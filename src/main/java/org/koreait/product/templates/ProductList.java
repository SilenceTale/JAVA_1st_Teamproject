package org.koreait.product.templates;

import org.koreait.global.Model;
import org.koreait.global.Template;
import org.koreait.global.libs.Utils;
import org.koreait.product.entities.Product;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 상품 목록 템플릿
 *
 */
public class ProductList implements Template {

    private List<Product> items;

    // ## 출력 담당하는 view ##
    @Override
    public void print() {
        System.out.println("상품목록");
        Utils.drawLine('-', 30);
        if (items != null && !items.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            for (Product item : items) {
                System.out.printf("상품번호: %d / 상품명: %s / 판매가: %d원 / 남은수량: %d개 / 등록일: %s%n", item.getSeq(), item.getName(), item.getPrice(), item.getStock(), formatter.format(item.getRegDt()));
            }
            return;
        }

        System.out.println("등록된 상품이 없습니다.");
    }

    @Override
    // ## Tempalte에 넘길 데이터가 있을때 model로 보내서 print() ##
    public void print(Model model) {
        Object data = model.getData();
        if (data != null) {
            items = (List<Product>)data;
        }

        print();
    }
}
