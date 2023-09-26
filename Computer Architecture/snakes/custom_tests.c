#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "asserts.h"
// Necessary due to static functions in state.c
#include "state.c"

/* Look at asserts.c for some helpful assert functions */

int greater_than_forty_two(int x) { return x > 42; }

bool is_vowel(char c) {
  char* vowels = "aeiouAEIOU";
  for (int i = 0; i < strlen(vowels); i++) {
    if (c == vowels[i]) {
      return true;
    }
  }
  return false;
}

/*
  Example 1: Returns true if all test cases pass. False otherwise.
    The function greater_than_forty_two(int x) will return true if x > 42. False otherwise.
    Note: This test is NOT comprehensive
*/
bool test_greater_than_forty_two() {
  int testcase_1 = 42;
  bool output_1 = greater_than_forty_two(testcase_1);
  if (!assert_false("output_1", output_1)) {
    return false;
  }

  int testcase_2 = -42;
  bool output_2 = greater_than_forty_two(testcase_2);
  if (!assert_false("output_2", output_2)) {
    return false;
  }

  int testcase_3 = 4242;
  bool output_3 = greater_than_forty_two(testcase_3);
  if (!assert_true("output_3", output_3)) {
    return false;
  }

  return true;
}

/*
  Example 2: Returns true if all test cases pass. False otherwise.
    The function is_vowel(char c) will return true if c is a vowel (i.e. c is a,e,i,o,u)
    and returns false otherwise
    Note: This test is NOT comprehensive
*/
bool test_is_vowel() {
  char testcase_1 = 'a';
  bool output_1 = is_vowel(testcase_1);
  if (!assert_true("output_1", output_1)) {
    return false;
  }

  char testcase_2 = 'e';
  bool output_2 = is_vowel(testcase_2);
  if (!assert_true("output_2", output_2)) {
    return false;
  }

  char testcase_3 = 'i';
  bool output_3 = is_vowel(testcase_3);
  if (!assert_true("output_3", output_3)) {
    return false;
  }

  char testcase_4 = 'o';
  bool output_4 = is_vowel(testcase_4);
  if (!assert_true("output_4", output_4)) {
    return false;
  }

  char testcase_5 = 'u';
  bool output_5 = is_vowel(testcase_5);
  if (!assert_true("output_5", output_5)) {
    return false;
  }

  char testcase_6 = 'k';
  bool output_6 = is_vowel(testcase_6);
  if (!assert_false("output_6", output_6)) {
    return false;
  }

  return true;
}

/* Task 4.1 */

bool test_is_tail() {
  char testcase_1 = 'w';
  bool output1 = is_tail(testcase_1);
  if (!assert_true("output1", output1)) {
    return false;
  }

  char testcase_2 = 'a';
  bool output2 = is_tail(testcase_2);
  if (!assert_true("output2", output2)) {
    return false;
  }

  char testcase_3 = 's';
  bool output3 = is_tail(testcase_3);
  if (!assert_true("output3", output3)) {
    return false;
  }

  char testcase_4 = 'd';
  bool output4 = is_tail(testcase_4);
  if (!assert_true("output4", output4)) {
    return false;
  }

  char testcase_5 = 'o';
  bool output5 = is_tail(testcase_5);
  if (!assert_false("output5", output5)) {
    return false;
  }
  
  char *random_testcase = "bcuyuhbnmnmcvbghftr6758-0;=;jmmcnzchj.xmnzx32jlko";
  char *p = random_testcase;
  bool random_output;
  for (int i = 0; i < strlen(random_testcase); i++) {
    random_output = is_tail(*p);
    if (!assert_false("random_output", random_output)) {
      return false;
    }
    p += 1;
  }

  return true;
}

bool test_is_head() {
  char testcase_1 = 'W';
  bool output1 = is_head(testcase_1);
  if (!assert_true("output1", output1)) {
    return false;
  }

  char testcase_2 = 'A';
  bool output2 = is_head(testcase_2);
  if (!assert_true("output2", output2)) {
    return false;
  }

  char testcase_3 = 'S';
  bool output3 = is_head(testcase_3);
  if (!assert_true("output3", output3)) {
    return false;
  }

  char testcase_4 = 'D';
  bool output4 = is_head(testcase_4);
  if (!assert_true("output4", output4)) {
    return false;
  }

  char testcase_5 = 'x';
  bool output5 = is_head(testcase_5);
  if (!assert_true("output5", output5)) {
    return false;
  }

  char testcase_6 = 'M';
  bool output6 = is_head(testcase_6);
  if (!assert_false("output6", output6)) {
    return false;
  }
  
  char *random_testcase = "RURccBNgQEyF5ZC7C5VX33N5,5NKd'I;R][UOfL";
  char *p = random_testcase;
  bool random_output;
  for (int i = 0; i < strlen(random_testcase); i++) {
    random_output = is_head(*p);
    if (!assert_false("random_output", random_output)) {
      return false;
    }
    p += 1;
  }

  return true;
}

bool test_is_snake() {
  char *testcases = "wasd^<v>WASDx";
  char *p = testcases;
  bool output;
  for (int i = 0; i < strlen(testcases); i++) {
    output = is_snake(*p);
    if (!assert_true("output", output)) {
      return false;
    }
    p += 1;
  }

  return true;
}

bool test_body_to_tail() {
  char testcase_1 = '^';
  char output_1 = body_to_tail(testcase_1);
  if (!assert_equals_char("output_1", 'w', output_1)) {
    return false;
  }

  char testcase_2 = '<';
  char output_2 = body_to_tail(testcase_2);
  if (!assert_equals_char("output_1", 'a', output_2)) {
    return false;
  }

  char testcase_3 = 'v';
  char output_3 = body_to_tail(testcase_3);
  if (!assert_equals_char("output_1", 's', output_3)) {
    return false;
  }

  char testcase_4 = '>';
  char output_4 = body_to_tail(testcase_4);
  if (!assert_equals_char("output_1", 'd', output_4)) {
    return false;
  }

  return true;
}

bool test_head_to_body() {
  char *testcases = "WASD";
  char *expected_outputs = "^<v>";
  char *p1 = testcases;
  char *p2 = expected_outputs;
  for (int i = 0; i < strlen(testcases); i++) {
    char result = head_to_body(*p1);
    if (!assert_equals_char("expected_outputs", *p2, result)) {
      return false;
    }
    p1 += 1;
    p2 += 1;
  }

  return true;
}

bool test_get_next_x() {
  unsigned int next_row_1 = get_next_row(3, 'v');
  if (!assert_equals_unsigned_int("next_row_1", 4, next_row_1)) {
    return false;
  }

  unsigned int next_row_2 = get_next_row(14, 's');
  if (!assert_equals_unsigned_int("next_row_2", 15, next_row_2)) {
    return false;
  }

  unsigned int next_row_3 = get_next_row(8, 'S');
  if (!assert_equals_unsigned_int("next_row_3", 9, next_row_3)) {
    return false;
  }

  unsigned int next_row_4 = get_next_row(12, 'w');
  if (!assert_equals_unsigned_int("next_row_4", 11, next_row_4)) {
    return false;
  }

  unsigned int next_row_5 = get_next_row(2, 'W');
  if (!assert_equals_unsigned_int("next_row_5", 1, next_row_5)) {
    return false;
  }

  unsigned int next_row_6 = get_next_row(19, '^');
  if (!assert_equals_unsigned_int("next_row_6", 18, next_row_6)) {
    return false;
  }

  char *random_char = "adAYHRGVDCZXQCBM9380'krnf/.ljri?';[XCZ]";
  for (int i = 0; i < strlen(random_char); i++) {
    unsigned int next_row_7 = get_next_row(20, *random_char);
    if (!assert_equals_unsigned_int("next_row_7", 20, next_row_7)) {
      return false;
    }
    random_char += 1;
  }

  return true;
}

bool test_get_next_y() {
  unsigned int next_col_1 = get_next_col(20, '<');
  if (!assert_equals_unsigned_int("next_col_1", 19, next_col_1)) {
    return false;
  }

  unsigned int next_col_2 = get_next_col(13, 'A');
  if (!assert_equals_unsigned_int("next_col_2", 12, next_col_2)) {
    return false;
  }

  unsigned int next_col_3 = get_next_col(8, 'a');
  if (!assert_equals_unsigned_int("next_col_3", 7, next_col_3)) {
    return false;
  }

  unsigned int next_col_4 = get_next_col(3, '>');
  if (!assert_equals_unsigned_int("next_col_4", 4, next_col_4)) {
    return false;
  }

  unsigned int next_col_5 = get_next_col(6, 'd');
  if (!assert_equals_unsigned_int("next_col_5", 7, next_col_5)) {
    return false;
  }

  unsigned int next_col_6 = get_next_col(14, 'D');
  if (!assert_equals_unsigned_int("next_col_6", 15, next_col_6)) {
    return false;
  }

  char *random_char = "wnjhbs52tgswpx;lf,.??[dprkl8*hen]";
  for (int i = 0; i < strlen(random_char); i++) {
    unsigned int next_col_7 = get_next_col(7, *random_char);
    if (!assert_equals_unsigned_int("next_col_7", 7, next_col_7)) {
      return false;
    }
    random_char += 1;
  }
  return true;
}

bool test_customs() {
  if (!test_greater_than_forty_two()) {
    printf("%s\n", "test_greater_than_forty_two failed.");
    return false;
  }
  if (!test_is_vowel()) {
    printf("%s\n", "test_is_vowel failed.");
    return false;
  }
  if (!test_is_tail()) {
    printf("%s\n", "test_is_tail failed");
    return false;
  }
  if (!test_is_head()) {
    printf("%s\n", "test_is_head failed");
    return false;
  }
  if (!test_is_snake()) {
    printf("%s\n", "test_is_snake failed");
    return false;
  }
  if (!test_body_to_tail()) {
    printf("%s\n", "test_body_to_tail failed");
    return false;
  }
  if (!test_head_to_body()) {
    printf("%s\n", "test_head_to_body failed");
    return false;
  }
  if (!test_get_next_x()) {
    printf("%s\n", "test_get_next_x failed");
    return false;
  }
  if (!test_get_next_y()) {
    printf("%s\n", "test_get_next_y failed");
    return false;
  }
  return true;
}

int main(int argc, char* argv[]) {
  init_colors();

  if (!test_and_print("custom", test_customs)) {
    return 0;
  }

  return 0;
}
