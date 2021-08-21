package love.simbot.example.listener;

import catcode.CatCodeUtil;
import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroupMsgRecall;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.annotation.OnPrivateMsgRecall;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.filter.MatchType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 私聊消息监听的示例类。
 * 所有需要被管理的类都需要标注 {@link Beans} 注解。
 * @author ForteScarlet
 */
@Beans
public class MyPrivateListen1 {

    /**
     * 通过依赖注入获取一个 "消息正文构建器工厂"。
     */
    @Depend
    private MessageContentBuilderFactory messageContentBuilderFactory;

    /**
     * 此监听函数监听一个私聊消息
     * 此方法上使用的是一个模板注解{@link OnPrivate}，
     * 其代表监听私聊。
     * 由于你监听的是私聊消息，因此参数中要有个 {@link PrivateMsg} 来接收这个消息实体。
     * 当然，你也可以使用 {@link love.forte.simbot.api.sender.MsgSender}，
     * 然后 {@code msgSender.SENDER}.
     */
//    @OnPrivate
//    @Filter(value = "hi", matchType = MatchType.STARTS_WITH)
//    public void replyPrivateMsg(PrivateMsg privateMsg, Sender sender) {
//        String accountCode = privateMsg.getAccountInfo().getAccountCode();
//        if (accountCode.equals("1944813269") || accountCode.equals("3319764103")) {
//
//
//            // 获取消息正文。
//            MessageContent msgContent = privateMsg.getMsgContent();
//
//
//            // 向 privateMsg 的账号发送消息，消息为当前接收到的消息。
//            sender.sendPrivateMsg(privateMsg, msgContent);
//
//            // 再发送一个表情ID为'9'的表情。
//            // 方法1：使用消息构建器构建消息并发送
//            // 在绝大多数情况下，使用消息构建器所构建的消息正文 'MessageContent'
//            // 是用来发送消息最高效的选择。
//            // 相对的，MessageContentBuilder所提供的构建方法是十分有限的。
//
//            // 获取消息构建器
//            MessageContentBuilder msgBuilder = messageContentBuilderFactory.getMessageContentBuilder();
//            // 通过.text(...) 向builder中追加一句话。
//            // 通过.face(ID) 向builder中追加一个表情。
//            // 通过.build() 构建出最终消息。
//            MessageContent msg = msgBuilder.text("表情：").face(9).build();
//
//            // 直接通过这个msg发送。
////            sender.sendPrivateMsg(privateMsg, msg);
//        }
//
//    }

    /**
     * 监听撤回消息
     * @param privateMsgRecall
     * @param sender
     */
    @OnPrivateMsgRecall
    public void PrivateMsgRecall(PrivateMsgRecall privateMsgRecall , Sender sender, BotManager botManager) {
        //获取当前机器人的账号
        BotInfo botInfo = privateMsgRecall.getBotInfo();
        String botAccountCode1 = botInfo.getAccountCode();
        String dirname = "D:\\simple-robot\\"+botAccountCode1;

//        System.out.println(botAccountCode1);

        //获取时间戳,精确到秒
        long timeStamp = privateMsgRecall.getTime();
        String t = String.valueOf(timeStamp/1000);
        String time = timeStampToDate(t, "yyyy-MM-dd HH:mm:ss");
        //获取撤回的消息的类型
        List<Neko> cats = privateMsgRecall.getMsgContent().getCats();
        //判断目标文件夹是否存在，不存在则创建
        File folder = new File(dirname);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            System.out.println(dirname+"文件夹以创建");
        } else {
            System.out.println(dirname+"文件夹已存在");
        }

        //判断目标文件是否存在，不存在则创建
        String filename = dirname+"\\PrivateMsgRecall.txt";
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(filename+"文件已创建");
        } else {
            System.out.println(filename+"文件已存在");
        }

        //获取撤回人员的基本信息
        AccountInfo accountInfo = privateMsgRecall.getAccountInfo();
        //账号
        String accountCode = accountInfo.getAccountCode();
        //头像
        String accountAvatar = accountInfo.getAccountAvatar();
        //昵称
        String accountNickname = accountInfo.getAccountNickname();
        //备注
        String accountRemark = accountInfo.getAccountRemark();

        //追加文件内容
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(filename, true);
            writer.write("撤回人员账号为: "+accountCode+"\n");
            writer.write("撤回人员昵称为: "+accountNickname+"\n");


            //        循环遍历并存入本地
            for (Neko cat:cats
            ) {
                //撤回的消息类型
                String catType = cat.getType();
                if(catType.equals("image")){
                    String url = cat.get("url");
                    writer.write("撤回的消息类型为图片，图片链接为: "+url+"\n");
                }
                else if(catType.equals("face")){
                    String id = cat.get("id");
                    writer.write("撤回的消息类型为qq表情，表情id为: "+id+"\n");
                }
                else if(catType.equals("text")){
                    String text = cat.get("text");
                    if(text!= null && !text.trim().isEmpty()){
                        writer.write("撤回的消息类型为text，内容为: "+text.trim()+"\n");
                    }
                }
            }
            writer.write("-----------------------------------------------------------"+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    /**
     * 监听群聊撤回消息
     * @param groupMsgRecall
     * @param sender
     * @param botManager
     */
    @OnGroupMsgRecall
    public void GroupMsgRecall(GroupMsgRecall groupMsgRecall , Sender sender, BotManager botManager) {
        //获取当前机器人的账号
        BotInfo botInfo = groupMsgRecall.getBotInfo();
        String botAccountCode1 = botInfo.getAccountCode();
        String dirname = "D:\\simple-robot\\"+botAccountCode1;

//        System.out.println(botAccountCode1);

        //获取撤回的消息的类型
        List<Neko> cats = groupMsgRecall.getMsgContent().getCats();
        //判断目标文件夹是否存在，不存在则创建
        File folder = new File(dirname);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            System.out.println(dirname+"文件夹以创建");
        } else {
            System.out.println(dirname+"文件夹已存在");
        }

        //判断目标文件是否存在，不存在则创建
        String filename = dirname+"\\GroupMsgRecall.txt";
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(filename+"文件已创建");
        } else {
            System.out.println(filename+"文件已存在");
        }

        //获取时间戳
        long timeStamp = groupMsgRecall.getTime();
        String t = String.valueOf(timeStamp/1000);
        String time = timeStampToDate(t, "yyyy-MM-dd HH:mm:ss");
        //获取群聊信息
        GroupInfo groupInfo = groupMsgRecall.getGroupInfo();
        String groupCode = groupInfo.getGroupCode();
        String groupName = groupInfo.getGroupName();
        //获取撤回人员的基本信息
        AccountInfo accountInfo = groupMsgRecall.getAccountInfo();
        //账号
        String accountCode = accountInfo.getAccountCode();
        //头像
        String accountAvatar = accountInfo.getAccountAvatar();
        //昵称
        String accountNickname = accountInfo.getAccountNickname();
        //备注
        String accountRemark = accountInfo.getAccountRemark();



        //追加文件内容
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(filename, true);
            writer.write("时间: "+time+"\n");
            writer.write("发生撤回消息所在的群群号为: "+groupCode+"\n");
            writer.write("发生撤回消息所在的群群名字为: "+groupName+"\n");
            writer.write("撤回人员账号为: "+accountCode+"\n");
            writer.write("撤回人员昵称为: "+accountNickname+"\n");

            //        循环遍历并存入本地
            for (Neko cat:cats
            ) {
                //撤回的消息类型
                String catType = cat.getType();
                if(catType.equals("image")){
                    String url = cat.get("url");
                    writer.write("撤回的消息类型为图片，图片链接为: "+url+"\n");
                }
                else if(catType.equals("face")){
                    String id = cat.get("id");
                    writer.write("撤回的消息类型为qq表情，表情id为: "+id+"\n");
                }
                else if(catType.equals("text")){
                    String text = cat.get("text");
                    if(text!= null && !text.trim().isEmpty()){
                        writer.write("撤回的消息类型为text，内容为: "+text.trim()+"\n");
                    }
                }
            }
            writer.write("-----------------------------------------------------------"+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStampToDate(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

}
