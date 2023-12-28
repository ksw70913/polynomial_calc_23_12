package com.ll;     // com.ll 폴더 안에 있음

public class Calc { //Calc 클래스
  public static int run(String exp) { // 10 + (10 + 5)  // 리턴 타입이 int인 static run 메소드 (매개변수는 String 타입)
    exp = exp.trim();  // exp 변수안에 trim기능(좌우 공백 없앰)
    exp = stripOuterBracket(exp); // exp 변수안에 stripOuterBracket(인자 exp) 메소드

    // 연산기호가 없으면 바로 리턴
    if (!exp.contains(" ")) return Integer.parseInt(exp);
  // exp안에 공백이 있지 않다면 연산자가 없는것이므로 exp를 int타입으로 바꿔 그대로 돌려준다
    // 밑에는 exp 안에 연산자가 있을 때 발동
    boolean needToMultiply = exp.contains(" * ");  //exp안에 *연산자가 있으면 true (boolean 타입이기 때문에 true or false)
    boolean needToPlus = exp.contains(" + ") || exp.contains(" - "); //exp안에 + 또는 - 가 있다면 true

    boolean needToCompound = needToMultiply && needToPlus; // 위에 두 변수를 충족한다면 true
    boolean needToSplit = exp.contains("(") || exp.contains(")"); //exp안에 괄호가 있다면 true

    if (needToSplit) {  // exp안에 괄호가 있을 때

      int splitPointIndex = findSplitPointIndex(exp); //findSplitPointIndex(인자exp)메소드 실행한 값을 int 타입 splitPointIndex안에 넣음
      //ex ) (10 + 20) * 3 일때
      String firstExp = exp.substring(0, splitPointIndex); // (10 + 20)
      String secondExp = exp.substring(splitPointIndex + 1); // 3

      char operator = exp.charAt(splitPointIndex); // *

      exp = Calc.run(firstExp) + " " + operator + " " + Calc.run(secondExp); // 재귀함수
      // exp 안에 (10+20) * (3) 을 넣어줌
      return Calc.run(exp); //그리고 다시 재귀해서 run 메소드

    } else if (needToCompound) { // 만약 exp안에 괄호가 없다면
      String[] bits = exp.split(" \\+ "); // String타입 배열 bits안에 exp를 +로 잘라서 넣어줌
      //ex) 10+20이면 bits[0]에 10, bits[1]에 20으로 int 타입으로 변환후 돌려줌
      return Integer.parseInt(bits[0]) + Calc.run(bits[1]); // TODO //
    } //리턴이 있는이유는 이 메소드의 리턴 타입이 int이기 때문
    if (needToPlus) { //만약 exp안에 괄호가 없다면
      exp = exp.replaceAll("\\- ", "\\+ \\-"); //exp안에 -를 전부 + -로 치환해줌

      String[] bits = exp.split(" \\+ "); //그다음에 String타입 배열 bits안에 +로 잘라서 넣어줌

      int sum = 0; //int 타입 sum 데이터값 0

      for (int i = 0; i < bits.length; i++) {
        sum += Integer.parseInt(bits[i]);
      }

      return sum;
    } else if (needToMultiply) {
      String[] bits = exp.split(" \\* ");

      int rs = 1;

      for (int i = 0; i < bits.length; i++) {
        rs *= Integer.parseInt(bits[i]);
      }
      return rs;
    }

    throw new RuntimeException("처리할 수 있는 계산식이 아닙니다");
  }

  private static int findSplitPointIndexBy(String exp, char findChar) {
    // 리턴 타입이 int static findSplitPointIndexBy(매개변수 exp, char 타입 findchar) 메서드
    int bracketCount = 0;
    // int 타입 bracketCount 변수 안에 0을 넣음
    for (int i = 0; i < exp.length(); i++) { //for 반복문 i가 0부터 1씩증가하고 string 타입 exp의 길이까지 i가 1씩증가)
      char c = exp.charAt(i);
      // exp문장에 index번호에 따른 단어를 char 타입으로 추출해 변수 c에 넣음
      if (c == '(') { //if 반복문 c가 '(' 일때
        bracketCount++; // bracketCount 1증가
      } else if (c == ')') { //if가 아닐때, c가 ')' 라면
        bracketCount--; // bracketCount 1감소
      } else if (c == findChar) { //c가 findChar일때
        if (bracketCount == 0) return i; // bracketCount가 0이라면 i를 charAt(i)에 넣어줌
      }
    }
    return -1; //아니라면 charAt(-1)를 넣어줌
  }

  private static int findSplitPointIndex(String exp) { //리턴 타입이 int인 static findSplitPointIndex(매개변수 String 타입 exp) 메소드
    int index = findSplitPointIndexBy(exp, '+');
  // findSplitPointIndexBy(인자 exp, char 타입'+') 메서드
    if (index >= 0) return index;
  // index가 0보다 크거나 같을때 index안에 그 index 값을 되돌려줌
    return findSplitPointIndexBy(exp, '*'); //아니라면 findchar 가 *일때 그 index 값을 되돌려줌
  }

  private static String stripOuterBracket(String exp) { // String 타입의 static stripOuterBracket (매개변수 exp) 메소드
    int outerBracketCount = 0;  // int 타입 outerBracketCount 변수안에 데이터값 0

    while (exp.charAt(outerBracketCount) == '(' && exp.charAt(exp.length() - 1 - outerBracketCount) == ')') {
      outerBracketCount++;
    }
  // while 반복문 exp.charAt(String 타입의 한 단어만 char타입으로 추출)(outerBracketCount{0})이 '(' 이고
    // exp.charAt(exp.length(){exp 문장의 길이}) -1 -outerBracketCount(0)이 ')' 이면 outerBracketCount1증가
    // 둘 중에 하나라도 성립하지않으면 while 반복문 탈출-> exp에 괄호가 있는지 체크하는 코드
    if (outerBracketCount == 0) return exp;
  // 0이면 괄호가 없어서 exp 문장 그대로 돌려준다

    return exp.substring(outerBracketCount, exp.length() - outerBracketCount);
  }
}
// exp에 괄호가 있으면 substring(문장을 index 번호로 끊어 추출){outerBracketCount부터 exp길이에 outerBracketCount부터 뺀것 까지)
// 괄호를 제외한 안에 있는 문장만 가져오는 코드