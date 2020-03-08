package zmz.handler.message.executor;

import cqrs.executor.CommandExecutor;
import zmz.handler.message.cmd.SelfMessageCommand;

public class SelfMessageCommandExecutor implements CommandExecutor<SelfMessageCommand>
{
    @Override
    public void executor(SelfMessageCommand selfMessageCommand)
    {

//        List<User> collect = clientHandlerList.stream()
//                .filter(i -> !i.getClientId().equals(message.getFromId()))
//                .map(ClientHandler::getUser)
//                .collect(Collectors.toList());
//
//        String s = JSON.toJSONString(collect);
//        Message m = new Message();
//        m.setMessage(s);
//        m.setType(MessageType.REPLY_ONLINE_USER);
//        handler.send(JSON.toJSONString(m));
//
//
//        collect.clear();
//        collect.add(handler.getUser());
//
//        //通知其他客户端有新用户上线
//        s = JSON.toJSONString(collect);
//        m.setMessage(s);
//        for (ClientHandler clientHandler : clientHandlerList) {
//            if (clientHandler.equals(handler)) {
//                // 跳过自己
//                continue;
//            }
//            // 对其他客户端发送消息
//            clientHandler.send(JSON.toJSONString(m));
//      }
    }
}
