package core;

import java.io.IOException;

public class IoContext {
    private static IoContext INSTANCE;
    private final IoRegistry ioRegistry;

    private IoContext(IoRegistry ioRegistry) {
        this.ioRegistry = ioRegistry;
    }

    public IoRegistry getIoRegistry() {
        return ioRegistry;
    }

    public static IoContext get() {
        return INSTANCE;
    }

    public static StartedBoot setup() {
        return new StartedBoot();
    }

    public static void close() throws IOException {
        if (INSTANCE != null) {
            INSTANCE.callClose();
        }
    }

    private void callClose() throws IOException {
        ioRegistry.close();
    }

    public static class StartedBoot {
        private IoRegistry ioRegistry;

        private StartedBoot() {

        }

        public StartedBoot ioProvider(IoRegistry ioRegistry) {
            this.ioRegistry = ioRegistry;
            return this;
        }

        public IoContext start() {
            INSTANCE = new IoContext(ioRegistry);
            return INSTANCE;
        }
    }
}
