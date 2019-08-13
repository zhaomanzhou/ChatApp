package bean.vo;

public enum MessageType
{
    //self  客户端连接后把自身信息发送给服务器
    //SIMPLE_MESSAGE 用户一对一聊天信息
    //GROUP_MESSAGE群聊信息
    //FILE 文件信息
    //QUERY  客户端向服务器查询
    //REPLY_ONLINE_USER  服务器端的返回信息
    SELF,SIMPLE_MESSAGE, GROUP_MESSAGE, FILE, QUERY, REPLY_ONLINE_USER,
}
