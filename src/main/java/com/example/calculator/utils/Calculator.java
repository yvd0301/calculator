package com.example.calculator.utils;

import java.util.*;


public class Calculator {

    private static Calculator calculator;

    private Stack<String> output;       // 계산결과 stack
    private Stack<operator> operators;  // 연산자 stack
    private Deque<String> dataQueue;    // client에서 받은 숫자, 연산자 Dequeue
    private StringBuffer equation;      // 계산식 저장
    private double previousResult;      // 이전 계산 결과 누적

    /**
     * Calculator 기본 생성
     */
    public Calculator() {
        previousResult = 0.0;
        equation = new StringBuffer();
        output = new Stack<String>();
        operators = new Stack<operator>();
        dataQueue = new ArrayDeque<>();
    }

    /**
     * Singleton 객체 반환
     *
     * @return calculator 객체
     */
    public static Calculator getInstance() {
        if (calculator == null) {
            calculator = new Calculator();
        }
        return calculator;
    }

    /**
     * 계산식 반환
     */
    public String getEquation() {
        return equation.toString();
    }

    /**
     * 계산할 데이터 입력
     */
    public void dataPush(String data) {
        dataQueue.addLast(data);
    }

    /**
     * dequeue 마지막 엘리먼트 부호 반전
     */
    public void reverseSign(String sign) {
        String lastElement = dataQueue.pollLast();
        if (lastElement != null && sign.equals("-")) {
            double d = Double.parseDouble(lastElement);
            lastElement = String.valueOf(d * -1);
            dataQueue.addLast(lastElement);
        }
    }

    /**
     * dataQueue out 메소드
     */
    public String dataPull() {
        StringBuffer data = new StringBuffer();
        while (!dataQueue.isEmpty()) {
//            data.append(dataQueue.poll());
            data.append(dataQueue.pollFirst());
        }
        return data.toString();
    }

    /**
     * 누적된 계산 결과값, 계산 데이터 Queue, stack 초기화
     */
    public void initialize() {
        previousResult = 0.0;
        equation.delete(0, equation.length());
        output.clear();
        operators.clear();
        dataQueue.clear();
    }

    /**
     * 중위 연산 표현식 후위 표기법으로 토큰화 하여 스택에 저장
     */
    private void processEquation(String formula) {

        // 계산식 저장
        equation.append(formula);

        // 피연산자와 연산자 토큰 하기 위해 사이에 공백 추가
        for (int i = 0; i < operator.operators.length(); i++) {
            String str = "" + operator.operators.charAt(i);
            formula = formula.replaceAll("[" + str + "]", " " + str + " ");
        }

        StringTokenizer tokenizer = new StringTokenizer(formula);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            if (isNumber(str)) {
                output.push(str);
            }
            else {
                operator op = operator.getOperator(str.charAt(0));
                if (!operators.isEmpty() && op.compareOrder(operators.peek()) <= 0)
                    output.push(operators.pop().toString());
                operators.push(op);
            }
        }
        while (!operators.isEmpty()) {
            output.push("" + operators.pop());
        }
    }

    /**
     * 후위 표기법 연산자 스택에서 두개 숫자 꺼내서 연산 수행
     */
    private double computeFomula() {

        Stack<Double> computeStack = new Stack<Double>();
        Stack<String> postfix = new Stack<String>();
        while (!output.isEmpty()) {
            postfix.push("" + output.pop());
            // 1 top
            // 2
            // + bottom
        }

        while (!postfix.isEmpty()) {
            String str = postfix.pop();
            if (isNumber(str)) {
                computeStack.push(Double.parseDouble(str));
            }
            else {
                operator op = operator.getOperator(str.charAt(0));
                // 10 % = 로 계산 결과 구하는 경우 예외처리
                if (op.getChar() == '%') {
                    double a = computeStack.pop();
                    computeStack.push(a / 100);
                } else {
                    double b = computeStack.pop(), a = computeStack.pop();
                    computeStack.push(op.operate(a, b));
                }
            }
        }

        return computeStack.pop();
    }

    /**
     * operand 숫자 여부 체크 helper
     */
    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            char c = s.charAt(0);
            if (operator.isOperator(c)) {
                return false;
            } else {
                throw new IllegalArgumentException("NumberFormatException Exception");
            }
        }
    }

    /**
     * 이전 계산 결과 setter
     */
    public void setPreviousResult(double result) {
        previousResult = result;
    }

    /**
     * 데이터 큐에 누적된 값에 대한 계산 결과 반환
     * C 버튼으로 clear 하지 않아 이전 결과가 있으면 이전 결과를 첫번째 피연산자로 계산
     */
    public double calculate(String formula) {
        if (previousResult == 0.0) {
            processEquation(formula);
        } else {
            // c 버튼 실행전 까지는 이전 결과 누적
            processEquation(previousResult + formula);
        }
        return computeFomula();
    }

    /**
     * 계산 연산자 enum 정의
     */
    private enum operator {

        /**
         * 사직 연산자 순서 정의
         * MAC 계산기의 경우 1+2*3 = 입력시 7 이므로 다음과 같은 연산 우선 순위 적용
         */
        mult(2, '*'), div(2, '/'), add(1, '+'), sub(1, 's'), percent(0, '%');

        private static final String operators = "*/+s%";
        private final int order;
        private final char chr;

        /**
         * 연산자 여부 체크 helper
         */
        public static boolean isOperator(char c) {
            for (int i = 0; i < operators.length(); i++)
                if (c == operators.charAt(i))
                    return true;
            return false;
        }

        operator(int order, char chr) {
            this.order = order;
            this.chr = chr;
        }

        /**
         * Operator getter
         */
        public char getChar() {
            return this.chr;
        }

        /**
         * char operator type 변환
         */
        public static operator getOperator(char c) {
            switch (c) {
                case '*':
                    return operator.mult;
                case '/':
                    return operator.div;
                case '+':
                    return operator.add;
                case 's':
                    return operator.sub;
                case '%':
                    return operator.percent;
                default:
                    throw new IllegalArgumentException("Operator Exception " + c);
            }
        }

        /**
         * a,b 계산 결과 반환
         */
        public double operate(double a, double b) {
            switch (this) {
                case mult:
                    return a * b;
                case div:
                    return a / b;
                case add:
                    return a + b;
                case sub:
                    return a - b;
                case percent:
                    return a % 100;
                default:
                    throw new ArithmeticException("Arithmetic Exception");
            }
        }

        @Override
        public String toString() {
            return "" + this.chr;
        }

        /**
         * 연산자 우선 순위 비교
         */
        public int compareOrder(operator o) {
            return ((Integer) this.order).compareTo(o.getOrder());
        }

        /**
         * 연산자 우선 순위 반환
         */
        private int getOrder() {
            return this.order;
        }
    }

}
