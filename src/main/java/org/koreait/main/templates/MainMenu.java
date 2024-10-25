package org.koreait.main.templates;

import org.koreait.global.Template;

public class MainMenu implements Template {

    /**
     * 메인 화면 메뉴 출력
     *
     */
    @Override
    public void print() {
        StringBuffer sb = new StringBuffer();
        sb.append("1. 상품목록\n");
                sb.append("2. 상품등록\n");
                    sb.append("3. 상품수정\n");

        System.out.println(sb);
    }
}
