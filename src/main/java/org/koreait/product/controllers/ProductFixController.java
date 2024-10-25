package org.koreait.product.controllers;

import org.koreait.global.BeanContainer;
import org.koreait.global.Controller;

import org.koreait.global.Model;
import org.koreait.global.Router;
import org.koreait.global.libs.Utils;
import org.koreait.global.validators.RequiredValidator;
import org.koreait.global.validators.TypeValidator;
import org.koreait.product.entities.Product;
import org.koreait.product.exceptions.ProductNotFoundException;
import org.koreait.product.services.ProductSaveService;
import org.koreait.product.templates.ProductForm;
import org.koreait.product.templates.ProductList;

import java.util.List;
import java.util.Scanner;

public class ProductFixController extends Controller implements RequiredValidator, TypeValidator {
    public ProductFixController() {

            // ## 사용자 요청 input
            // 한개의 값 비교
            // 값 1개 들어옴(input) 나가는 값(output) 없음 ##
            setInputProcess(input -> {
                /* 유효성 검사 S */
                if (!check(input)) { // 필수 항목 체크
                    return;
                }

                // ## 숫자 체크 ##
                if (!isNumber(input)) {
                    System.out.println("상품 번호는 숫자만 입력하세요.");
                    return;
                }

                /* 유효성 검사 E */

                // 선택한 상품 번호와 함께 상품 상세로 이동
                // ## 유효성 검사 통과시 상품 데이터 넘겨줌
                // Model에 상품 번호를 Long값으로 넘겨줌 ##
                Utils.loadController(ProductViewController.class, new Model(Long.parseLong(input)));


            });

        //없는 값이라면 ProductNotFoundException으로 던지기
        setPromptProcess(() -> {
            Object data = getData();
            if (data == null) {
                throw new ProductNotFoundException();
            }

            // ## 공통적으로 선 긋기 ##
            Utils.drawLine('-', 30);

            // ## 데이터 클래스 ##
            Scanner sc = Router.sc;
            Product item = new Product();

            // ## 함수형 인터페이스 3개(상품명 판매가 재고) 열린 개행 ##

            // 상품명
            String name = Utils.getString("상품명", "수정할 상품명을 입력하세요.");
            item.setName(name);

            // 판매가
            // ## 형식 숫자인지 검증 ##
            int price = Utils.getNumber("판매가", "수정할 판매가를 입력하세요.");
            item.setPrice(price);

            // 재고
            // ## 형식 숫자인지 검증
            // 재고 입력하지 않으면 입력 요청 문구 나오게 예외 ##
            int stock = Utils.getNumber("재고", "수정할 재고를 입력하세요.");
            item.setStock(stock);

            // 상품 정보 저장 처리
            // ## 처리할 수 있는 기능과 연결(중재)
            // 상품 추가 OR 수정
            // getBean(기능이기때문에 싱글톤으로 객체 생성) ##
            ProductSaveService saveService = BeanContainer.getBean(ProductSaveService.class);
            // ## save해서 상품 저장 ##
            saveService.save(item, false);

            System.out.println("상품이 수정되었습니다.");
            // 저장 이후에 상품 목록으로 페이지 이동

            // ## 작업 끝난후 다시 상품목록(List)으로 이동 시켜줌 ##
            Utils.loadController(ProductListController.class);
        });
    }

    // ##Controller에서 상속받은 추상 메서드들 오버라이딩
    // getPromptText 오버라이딩해 출력값 변경 ##
    @Override
    protected String getPromptText() {
        return "수정할 상품 정보를 입력하세요(메인 메뉴: M, 종료: Q).\n";
    }

    // ## 각 컨트롤러에 맞게 view 재정의 ##
    @Override
    public void view()  {
        Utils.loadTpl(ProductForm.class);


    }
}


