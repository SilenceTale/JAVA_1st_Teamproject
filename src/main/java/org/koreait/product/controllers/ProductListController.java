package org.koreait.product.controllers;

import org.koreait.global.BeanContainer;
import org.koreait.global.Controller;
import org.koreait.global.Model;
import org.koreait.global.libs.Utils;
import org.koreait.global.validators.RequiredValidator;
import org.koreait.global.validators.TypeValidator;
import org.koreait.product.entities.Product;
import org.koreait.product.services.ProductInfoService;
import org.koreait.product.templates.ProductList;

import java.util.List;

/**
 * 상품 목록
 *  - 상품목록에서 상품 번호를 입력하면 상품 상세보기로 이동
 *
 */
public class ProductListController extends Controller implements TypeValidator, RequiredValidator {
    public ProductListController() {

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
    }

    @Override
    protected String getPromptText() {
        return "조회할 상품번호를 입력하세요(메인메뉴: M, 종료: Q):";
    }

    @Override

    // ## 요청받은 상품 목록 데이터 출력으로 오버라이딩
    // 단 기능이므로 여러개 있을 필요 없어 싱글톤(getBean) ##
    public void view() {
        ProductInfoService service = BeanContainer.getBean(ProductInfoService.class);
        List<Product> items = service.getList();

        // 템플릿 로드 및 상품 목록 데이터 전송
        // ## 바로 위의 컨트롤러에서 넘겨준 데이터(items)
        // ProductList라는 View 템플릿이 출력해줌 ##
        Utils.loadTpl(ProductList.class, new Model(items));
    }
}
