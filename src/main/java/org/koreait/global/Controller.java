package org.koreait.global;

import org.koreait.global.libs.Utils;
import org.koreait.main.controllers.LoginController;
import org.koreait.main.controllers.ProductBranchController;
import org.koreait.member.entities.Accession;
import org.koreait.product.controllers.ProductFixController;

import java.util.function.Consumer;

/**
 * Controller 클래스
 *      모든 메뉴는 Controller 클래스를 상속 받아 정의 합니다.
 *      Controller 클래스는 사용자의 요청(예 - 메뉴 선택, 값 입력 등)이 유입되고
 *      이를 처리할 수 있는 서비스 객체와 화면 출력을 서로 연결해 주는 역할을 하게 됩니다.
 *      컨트롤러에서는 고정된 실행 절차가 정의되어 있고 이에 따라 실행 됩니다.
 *      이는 템플릿 메서드 패턴이 적용된 사례
 *      run() 메서드에는 실행 절차가 정의 되어 있습니다.
 *          실행 순서
 *          1) common() : 화면 상단 공통 출력 부분
 *          2) view() : 각 컨트롤러마다 다르게 출력될 부분 정의 - 상속 받은 클래스가 정의
 *          3) prompt() : 사용자의 입력을 받는 부분, 기본 화면은 메뉴 선택이지만 각 컨트롤러에서 새롭게 정의하면 해당 메뉴에 맞는 입력으로 새롭게 정의 가능
 */

// ## 추상 클래스
// 모든 컨트롤러의 상위 클래스로 체계 구성 ##

public abstract class Controller {

    // ## 컨트롤러가 데이터를 넘기길때 값을 담기위해 data 만듬
    // 어떤 데이터인지 모르니 다형성 활용해 Object 자료형 ##
    private Object data; // 컨트롤러 전환시 전달할 데이터

    // 컨트롤러마다 사용자 입력은 다르게 처리되므로 열린 기능 형태로 함수형 인터페이스를 지정합니다.
    private Consumer<String> inputProcess;

    /**
     * 컨트롤러에 따라서 입력을 여러개 받는 경우, 예를 들면 상품 등록
     * 이때는 프롬프트 입력을 하나 등록하고 여기에 여러개 정의, 따로 없다면
     * 기본 한개의 입력을 받는다.
     */

    // ## Runnable
    private Runnable promptProcess;

    // ## 모든 컨트롤러 공통 ##
    // 공통 출력 부분
    public void common() {
        Utils.drawLine('-', 30);
        System.out.printf("상품 관리 프로그램 v1.0.0\n");
        Utils.drawLine('-', 30);
    }

    // ## 추상 메서드
    // 모든 컨트롤러마다 다르게 출력될테니 추상메서드로 정의후
    // 하위 클래스에서 반드시 구현 ##
    public abstract void view(); // 화면 구성

    /**
     * 사용자 입력 문구
     *  - 화면에 따라서 입력 문구가 다른 경우 하기 메서드를 재정의 한다.
     *  - 그렇지 않다면 기본 문구 - 메뉴를 선택하세요(종료:Q): 출력 된다.
     * @return
     */

    // getPromptText default 문구 설정
    // protected (상속) - 어차피 내부적으로만 사용할거라
    // 오버라이드 통해서 바꿀 수 있게 유연성
    protected String getPromptText() {
        return "메뉴를 선택하세요(종료:Q):";
    }

    /**
     * 컨트롤러 전환시 전달할 데이터
     *
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 컨트롤러 전환시 전달받은 데이터
     *
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * 사용자 입력 처리
     *
     * @param inputProcess
     */

    // ## 상속 받은 하위클래스가 Consumer에 들어가는 값을 정해진 로직(내가 정의한=람다식)으로 재정의 ##

    protected void setInputProcess(Consumer<String> inputProcess) {
        this.inputProcess = inputProcess;
    }

    /**
     * 컨트롤러별 입력 새로 정의하는 경우 Runnable 인터페이스 구현체 추가
     *
     * @param promptProcess
     */
    protected void setPromptProcess(Runnable promptProcess) {
        this.promptProcess = promptProcess;
    }

    /**
     * 사용자 입력 데이터 처리
     * - 컨트롤러마다 처리는 다르므로 컨트롤러 마다 정의
     *
     */

    // ## 함수형 interface
    // 입력 받은 텍스트가 Q도 M도 아닐때
    protected void process(String input) {
        if (inputProcess != null) {

            // ## 구현 내용이 없는 interface
            // 1회용 객체
            // 그때그때 처리 내용이 달라서
            // 내가 추가하는 로직에 따라 처리를 다르게 하기 위해
            // Consumer.accept 사용 ##
            inputProcess.accept(input);
        }
    }

    /**
     * 사용자 입력 공통
     * - 사용자에게 입력을 받는 문구는 각 컨트롤러마다 달라질 수 있으므로
     * - getPromptText()를 하위 클래스에서 재정의하여 변경할 수 있습니다. 변경하지 않는다면 기본 메뉴 선택 문구로 출력이 됩니다.
     */
    // ## 사용자의 입력을 받는 공통 프롬포트##
    public void prompt() {
        Utils.drawLine('-', 30);

        // ## 그때그떄 사용자에게 출력될 문구가 다르게
        // EX) 조회할 상품번호를 입력하세요
        // 메뉴를 선택해주세요 등등
        System.out.print(getPromptText());

        // promptProcess가 null 이면 한개씩 입력 받고
        if (promptProcess == null) {

            // ## 그때그때 다른 입력받은 텍스트 ##
            String input = Router.sc.nextLine();


            // ## Q와 M은 언제든지 공통인 텍스트 ##

            // 입력 데이터 중 Q(대소문자 구분 없음)가 유입 되면 콘솔 프로그램 종료
            if (input.toUpperCase().equals("Q") || input.equals("ㅂ")) {
                System.out.println("종료합니다.");
                System.exit(0);
            } else if (input.toUpperCase().equals("M") || input.equals("ㅡ")) {
                // 입력 데이터가 M(대소문자 구분 없음)가 유입되면 메인 메뉴로 이동
                Utils.loadController(ProductBranchController.class);

            } else if (input.toUpperCase().equals("F")|| input.equals("ㄹ")) {
                //# 입력 데이터가 F(대소문자 구분 없음)가 유입되면 수정 메뉴로 이동
                Utils.loadController(ProductFixController.class);
            }

            else if (input.toUpperCase().equals("O") || input.equals("ㅐ")) {
                Accession acc = BeanContainer.getBean(Accession.class);
                acc.setLoginCheck(false);
                Utils.loadController(LoginController.class);
            }

            /**
             *  입력에 대한 처리는 컨트롤러 마다 달라질 수 있으므로 값만 넘겨주고
             *  각 상속 받은 컨트롤러에서 등록한 Consumer<String> inputProcess 에서 처리한다.
             *  따라서 process는 inputProcess.accept(String input)에 사용자가 입력한 값만 넘겨주면서 호출해 준다.
             */
            // ## 항상 다르게 입력받은 텍스트가
            // Q OR M이 아닐경우 값만 입력해 다른곳에 처리 넘김
            // 한개를 입력 받을때에는 적절 ##
            process(input); // 입력 처리

            // ## promptProcess가 null이 아닐경우 여러개 입력받으려고 (상품 등록) ##
        } else {
            promptProcess.run();
        }
    }

    /**
     * 콘솔 프로그램은 컨트롤러가 실행되는 절차가 고정되어 있으며 이는 변경이 불가 하다. 
     * 따라서 final로 하위 클래스가 재정의 하지 못하도록 고정합니다.
     * 실행 및 출력 순서는
     * 1) common() : 공통 화면
     * 2) view() : 하위클래스에서 구현한 view() 출력 
     * 3) prompt() : 사용자 입력 화면
     *
     * 이렇게 실행 절차를 고정해 놓은 함수를  템플릿 메서드라고 하며, 주로 추상 클래스에서 구현을 하므로 이를
     * 추상 템플릿 메서드 패턴이라고 합니다.
     */

    // ##################################
    // ## Controller 연산 절차 고정!!!!!!! #
    // ################################# #
    // final 메서드 = 재정의 불가능
    public final void run() throws Exception {
        common();
        view();
        prompt();
    }
}