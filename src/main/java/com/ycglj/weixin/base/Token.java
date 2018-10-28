package com.ycglj.weixin.base;

/**
* ����: Token </br>
* ����:  ƾ֤  </br>
* ������Ա�� souvc </br>
* ����ʱ�䣺  2015-11-27 </br>
* �����汾��V1.0  </br>
 */
public class Token {
    // �ӿڷ���ƾ֤
    private String accessToken;
    // ƾ֤��Ч�ڣ���λ����
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
