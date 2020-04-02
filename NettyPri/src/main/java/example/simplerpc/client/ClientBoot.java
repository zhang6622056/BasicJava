package example.simplerpc.client;

import example.simplerpc.service.UserService;





/**
 *
 * rpc 客户端启动类
 * @author Nero
 * @date 2020-04-02
 * *@param: null
 * @return 
 */
public class ClientBoot {

    public static void main(String[] args) {
        UserService userService = (UserService) ClientRpcProxy.create(UserService.class);
        System.out.println(userService.getUsername());
    }



}
