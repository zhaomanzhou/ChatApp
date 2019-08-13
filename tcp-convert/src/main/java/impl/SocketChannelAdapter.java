package impl;



import core.IoArgs;
import core.IoRegistry;
import core.Receiver;
import core.Sender;
import util.CloseUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketChannelAdapter implements Sender, Receiver, Cloneable {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final SocketChannel channel;
    private final IoRegistry ioRegistry;
    private final OnChannelStatusChangedListener listener;

    private IoArgs.IoArgsEventListener receiveIoEventListener;
    private IoArgs.IoArgsEventListener sendIoEventListener;

    public SocketChannelAdapter(SocketChannel channel, IoRegistry ioRegistry,
                                OnChannelStatusChangedListener listener) throws IOException {
        this.channel = channel;
        this.ioRegistry = ioRegistry;
        this.listener = listener;

        channel.configureBlocking(false);
    }


    @Override
    public boolean receiveAsync(IoArgs.IoArgsEventListener listener) throws IOException {
        if (isClosed.get()) {
            throw new IOException("Current channel is closed!");
        }

        receiveIoEventListener = listener;

        return ioRegistry.registerInput(channel, inputCallback);
    }

    @Override
    public boolean sendAsync(IoArgs args, IoArgs.IoArgsEventListener listener) throws IOException {
        if (isClosed.get()) {
            throw new IOException("Current channel is closed!");
        }

        sendIoEventListener = listener;
        // 当前发送的数据附加到回调中
        outputCallback.setAttach(args);
        return ioRegistry.registerOutput(channel, outputCallback);
    }

    @Override
    public void close() throws IOException {
        if (isClosed.compareAndSet(false, true)) {
            // 解除注册回调
            ioRegistry.unRegisterInput(channel);
            ioRegistry.unRegisterOutput(channel);
            // 关闭
            CloseUtils.close(channel);
            // 回调当前Channel已关闭
            listener.onChannelClosed(channel);
        }
    }

    private final IoRegistry.HandleInputCallback inputCallback = new IoRegistry.HandleInputCallback() {
        @Override
        protected void canProviderInput() {
            if (isClosed.get()) {
                return;
            }

            IoArgs args = new IoArgs();
            IoArgs.IoArgsEventListener listener = SocketChannelAdapter.this.receiveIoEventListener;

            if (listener != null) {
                listener.onStarted(args);
            }

            try {
                // 具体的读取操作
                if (args.read(channel) > 0 && listener != null) {
                    // 读取完成回调
                    listener.onCompleted(args);
                } else {
                    throw new IOException("Cannot read any data!");
                }
            } catch (IOException ignored) {
                CloseUtils.close(SocketChannelAdapter.this);
            }


        }
    };


    private final IoRegistry.HandleOutputCallback outputCallback = new IoRegistry.HandleOutputCallback() {
        @Override
        protected void canProviderOutput(Object attach) {
            if (isClosed.get()) {
                return;
            }

            sendIoEventListener.onCompleted(null);
        }
    };


    public interface OnChannelStatusChangedListener {
        void onChannelClosed(SocketChannel channel);
    }
}
