import java.util.*;
/*
support pending:

- uniary operator 
- ternary operator

*/
public class BasicCalculator {
    public static void main(String[] args) {
        new Calculator("1 + 2").eval(); // 3
        new Calculator("1 + 2 * 3").eval(); // 7
        new Calculator("( 1 + 2) * 3").eval(); // 9
        new Calculator("1 * 2 + 3").eval(); // 5
        new Calculator("27 / 9 / 3").eval(); // 1
        new Calculator("(1+(4+5+2)-3)+(6+8)").eval(); // 23
        new Calculator("2 - (    -2)").eval(); // 3
    }

    static class Calculator {
        String str;

        public Calculator(String str) {
            this.str = str;
            System.out.print("infix: " + str);

        }

        public int eval() {
            Queue<Object> q = buildPostNotation();
            Stack<Integer> stk = new Stack<>();

            while (!q.isEmpty()) {
                Object cur = q.poll();
                if (cur instanceof Integer) {
                    stk.push((int) cur);
                } else {
                    int b = stk.pop();
                    int a = stk.pop();
                    stk.push(operate(a, b, (char) cur));
                }
            }
            int ans = stk.pop();
            System.out.println("   ans: " + ans);
            return ans;
        }

        private int operate(int a, int b, char c) {
            if (c == '+') return a + b;
            if (c == '-') return a - b;
            if (c == '*') return a * b;
            if (c == '/') return a / b;
            return 0;
        }

        private Queue<Object> buildPostNotation() {
            Deque<Object> dq = new LinkedList<>();
            Stack<Character> op = new Stack<>();
            Integer num = null;

            for (char c : str.toCharArray()) {
                if (c == ' ')
                    continue;
                if (Character.isDigit(c)) {
                    if (num == null)
                        num = 0;
                    num *= 10;
                    num += c - '0';
                    continue;
                }
                if (num != null) {
                    dq.addLast(num);
                    num = null;
                }
                if (c == '(') {
                    op.push('(');
                } else if (c == ')') {
                    while (!op.isEmpty() && op.peek() != '(')
                        dq.addLast(op.pop());
                    op.pop();
                } else {
                    while (!op.isEmpty() && getOperatorPriority(op.peek()) <= getOperatorPriority(c))
                        dq.addLast(op.pop());
                    op.add(c);
                }
            }
            if (num != null)
                dq.addLast(num);
            while (!op.isEmpty())
                dq.addLast(op.pop());

            System.out.print("  postFix: " + dq);
            return dq;
        }

        int getOperatorPriority(char c) {
            if (c == '*' || c == '/')
                return 3;
            if (c == '-' || c == '+')
                return 4;
            return 10;
        }

    }
}
