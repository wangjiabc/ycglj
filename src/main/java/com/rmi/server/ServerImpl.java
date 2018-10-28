package com.rmi.server;

//继承该类， UnicastRemoteObject，暴露远程服务  
public class ServerImpl implements Server {  


  @Override  
  public String helloWorld(String name) {  
      // TODO Auto-generated method stub  
      return name + ",aaaaaaaaaaaaaaaa";  
  }  


}  