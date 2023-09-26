#include "state.h"

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "snake_utils.h"

/* Helper function definitions */
static void set_board_at(game_state_t* state, unsigned int row, unsigned int col, char ch);
static bool is_tail(char c);
static bool is_head(char c);
static bool is_snake(char c);
static char body_to_tail(char c);
static char head_to_body(char c);
static unsigned int get_next_row(unsigned int cur_row, char c);
static unsigned int get_next_col(unsigned int cur_col, char c);
static void find_head(game_state_t* state, unsigned int snum);
static char next_square(game_state_t* state, unsigned int snum);
static void update_tail(game_state_t* state, unsigned int snum);
static void update_head(game_state_t* state, unsigned int snum);

static void create_map(char** board, unsigned int num_rows, unsigned int num_col);
static void create_line_of_full_walls(char** board, unsigned int num_col);
static void creat_leftright_two_walls(char** b, unsigned int n_col);
static void malloc_board(char** b, unsigned int n_rows, unsigned int num_col);
static void print_map(char** b);
static void put_fruit_in_map(char** b, char fruit, unsigned int row, unsigned int col);
static void put_snake_in_map(char** b, struct snake_t *s, unsigned int row, unsigned int col);
static char* move_pointer_to(char **b, unsigned int row, unsigned int col);

static void free_strings_in_array(game_state_t* state);
static void free_array_of_snakes(game_state_t* state);

static unsigned int count_number_of_rows(char *filename);
static int count_number_of_char(FILE *fptr);
static unsigned int count_number_of_char_unsigned(FILE *fptr);
static void throw_enter(char *str);

snake_t* find_snake(game_state_t *state, unsigned int snum);
unsigned int count_snakes(game_state_t* state);
void fill_in_tail(game_state_t* state, unsigned int snum);

/* Global variables */
char *wall = "#";
char *space = " ";


/* Task 1 */
game_state_t* create_default_state() {
  game_state_t *game = (game_state_t *) malloc(sizeof(game_state_t));
  game->num_rows = 18;
  unsigned int num_col = 20;
  
  snake_t *default_snake = (snake_t *) malloc(sizeof(snake_t));
  default_snake->tail_row = 2;
  default_snake->tail_col = 2;
  default_snake->head_row = 2;
  default_snake->head_col = 4;
  default_snake->live = true;
  game->num_snakes = 1;
  game->snakes = default_snake;
  char fruit = '*';

  game->board = (char **) malloc(sizeof(char *) * game->num_rows);
  malloc_board(game->board, game->num_rows, 20);
  create_map(game->board, game->num_rows, num_col);
  put_fruit_in_map(game->board, fruit, 2, 9);
  put_snake_in_map(game->board, default_snake, 2, 2);

  return game;
}

void malloc_board(char** b, unsigned int n_rows, unsigned int n_char) {
  for (unsigned int  i = 0; i < n_rows; i++) {
    *b = (char *) malloc(sizeof(char) * n_char + 1);
    b += 1;
  }
}

void create_map(char** b, unsigned int n_rows, unsigned int n_col) {
  create_line_of_full_walls(b, n_col);
  b += 1;
  for (unsigned int i = 0; i < n_rows - 2; i++) {
    creat_leftright_two_walls(b, n_col);
    b += 1;
  }
  create_line_of_full_walls(b, n_col);
}

void creat_leftright_two_walls(char** b, unsigned int n_col) {
  char *p = *b;
  strcpy(p, "#");
  for (unsigned int i = 0; i < n_col - 1; i++) {
    p += 1;
    strcpy(p, " ");
  }
  strcpy(p, "#");
}

void create_line_of_full_walls(char** b, unsigned int n_col) {
  char *p = *b;
  for (unsigned int  i = 0; i < n_col; i++) {
    strcpy(p, "#");
    p += 1;
  }
}

void put_snake_in_map(char** b, struct snake_t *s, unsigned int row, unsigned int col) {
  char *p = move_pointer_to(b, s->tail_row, s->tail_col);
  *p = 'd';
  p += 1;
  *p = '>';
  p  = move_pointer_to(b, s->head_row, s->head_col);
  *p = 'D';
}

void put_fruit_in_map(char** b, char fruit, unsigned int row, unsigned int col) {
  char *p = move_pointer_to(b, row, col);
  *p = fruit;
}

/* Return a pointer pointing to the position (row, col) */
char* move_pointer_to(char **b, unsigned int row, unsigned int col) {
  for (unsigned int i = 0; i < row; i++) {
    b += 1;
  }

  char *p = *b;
  for (unsigned int j = 0; j < col; j++) {
    p += 1;
  }

  return p;
}

/* Just for debugging purpose */
void print_map(char** b) {
  for (unsigned int i = 0; i < 18; i++) {
    printf("%s\n", *b);
    b += 1;
  }
}

/* Task 2 */
void free_state(game_state_t* state) {
  free(state->snakes);
  free_strings_in_array(state);
  free(state->board);
  free(state);
  return;
}

void free_array_of_snakes(game_state_t* state) {
  snake_t *s = state->snakes;
  for (int i = 0; i < state->num_snakes; i++) {
    free(s);
    s += 1;
  }
}

void free_strings_in_array(game_state_t* state) {
  char **b = state->board;
  for (int i = 0; i < state->num_rows; i++) {
    free(*b);
    b += 1;
  }
}

/* Task 3 */
void print_board(game_state_t* state, FILE* fp) {
  char **b = state->board;
  for (unsigned int i = 0; i < state->num_rows; i++) {
    fprintf(fp, "%s\n", *b);
    b += 1;
  }
  return;
}

/*
  Saves the current state into filename. Does not modify the state object.
  (already implemented for you).
*/
void save_board(game_state_t* state, char* filename) {
  FILE* f = fopen(filename, "w");
  print_board(state, f);
  fclose(f);
}

/* Task 4.1 */

/*
  Helper function to get a character from the board
  (already implemented for you).
*/
char get_board_at(game_state_t* state, unsigned int row, unsigned int col) {
  return state->board[row][col];
}

/*
  Helper function to set a character on the board
  (already implemented for you).
*/
static void set_board_at(game_state_t* state, unsigned int row, unsigned int col, char ch) {
  state->board[row][col] = ch;
}

/*
  Returns true if c is part of the snake's tail.
  The snake consists of these characters: "wasd"
  Returns false otherwise.
*/
static bool is_tail(char c) {
  if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
    return true;
  }
  return false;
}

/*
  Returns true if c is part of the snake's head.
  The snake consists of these characters: "WASDx"
  Returns false otherwise.
*/
static bool is_head(char c) {
    if (c == 'W' || c == 'A' || c == 'S' || c == 'D' || c == 'x') {
    return true;
  }
  return false;
}

/*
  Returns true if c is part of the snake.
  The snake consists of these characters: "wasd^<v>WASDx"
*/
static bool is_snake(char c) {
  if (is_head(c) || is_tail(c) || (c == '^' 
        || c == '<' || c == 'v' || c == '>')) {
        return true;
    }
  return false;
}

/*
  Converts a character in the snake's body ("^<v>")
  to the matching character representing the snake's
  tail ("wasd").
*/
static char body_to_tail(char c) {
  switch(c) {
    case '^':
      c = 'w';
      break;
    case '<':
      c = 'a';
      break;
    case 'v':
      c = 's';
      break;
    case '>':
      c = 'd';
      break;
  }
  return c;
}

/*
  Converts a character in the snake's head ("WASD")
  to the matching character representing the snake's
  body ("^<v>").
*/
static char head_to_body(char c) {
    switch(c) {
    case 'W':
      c = '^';
      break;
    case 'A':
      c = '<';
      break;
    case 'S':
      c = 'v';
      break;
    case 'D':
      c = '>';
      break;
  }
  return c;
}

/*
  Returns cur_row + 1 if c is 'v' or 's' or 'S'.
  Returns cur_row - 1 if c is '^' or 'w' or 'W'.
  Returns cur_row otherwise.
*/
static unsigned int get_next_row(unsigned int cur_row, char c) {
  if (c == 'v' || c == 's' || c == 'S') {
    return cur_row + 1;
  } else if (c == '^' || c == 'w' || c == 'W') {
    return cur_row - 1;
  } else {
    return cur_row;
  }
}

/*
  Returns cur_col + 1 if c is '>' or 'd' or 'D'.
  Returns cur_col - 1 if c is '<' or 'a' or 'A'.
  Returns cur_col otherwise.
*/
static unsigned int get_next_col(unsigned int cur_col, char c) {
    if (c == '>' || c == 'd' || c == 'D') {
    return cur_col + 1;
  } else if (c == '<' || c == 'a' || c == 'A') {
    return cur_col - 1;
  } else {
    return cur_col;
  }
}

/*
  Task 4.2

  Helper function for update_state. Return the character in the cell the snake is moving into.

  This function should not modify anything.
*/
static char next_square(game_state_t* state, unsigned int snum) {
  snake_t *p = state->snakes;
  snake_t *snake = p + snum;
  unsigned int head_row = snake->head_row;
  unsigned int head_col = snake->head_col;
  char c = get_board_at(state, head_row, head_col);

  unsigned int next_row = get_next_row(head_row, c);
  unsigned int next_col = get_next_col(head_col, c);
  return get_board_at(state, next_row, next_col);
}

/*
  Task 4.3

  Helper function for update_state. Update the head...

  ...on the board: add a character where the snake is moving

  ...in the snake struct: update the row and col of the head

  Note that this function ignores food, walls, and snake bodies when moving the head.
*/
static void update_head(game_state_t* state, unsigned int snum) {
  snake_t *snake = state->snakes + snum;
  unsigned int head_row = snake->head_row;
  unsigned int head_col = snake->head_col;
  char head = get_board_at(state, head_row, head_col);
  char body = head_to_body(head);

  unsigned int next_row = get_next_row(head_row, head);
  unsigned int next_col = get_next_col(head_col, head);

  set_board_at(state, next_row, next_col, head); // Update on the board
  set_board_at(state, head_row, head_col, body);
  snake->head_row = next_row; // Update row and col of the snake head
  snake->head_col = next_col;
  return;
}

/*
  Task 4.4

  Helper function for update_state. Update the tail...

  ...on the board: blank out the current tail, and change the new
  tail from a body character (^<v>) into a tail character (wasd)

  ...in the snake struct: update the row and col of the tail
*/
static void update_tail(game_state_t* state, unsigned int snum) {
  snake_t *snake = state->snakes + snum;
  unsigned int tail_row = snake->tail_row;
  unsigned int tail_col = snake->tail_col;
  char old_tail = get_board_at(state, tail_row, tail_col);

  unsigned int next_row = get_next_row(tail_row, old_tail);
  unsigned int next_col = get_next_col(tail_col, old_tail);

  char body = get_board_at(state, next_row, next_col);
  char new_tail = body_to_tail(body);

  set_board_at(state, tail_row, tail_col, ' '); // Update on the board
  set_board_at(state, next_row, next_col, new_tail); 
  
  snake->tail_row = next_row; // Update row and col of the snake head
  snake->tail_col = next_col;
  return;
}

/* Task 4.5 */
void update_state(game_state_t* state, int (*add_food)(game_state_t* state)) {
  unsigned int snum_total = state->num_snakes;
  
  for (unsigned int snum = 0; snum < snum_total; snum++) {
    snake_t *snake = state->snakes + snum;
    char next = next_square(state, snum);
    if (next == '#' || is_snake(next)) { // If the snake crashes into a wall or the body of a snake
      snake->live = false;
      set_board_at(state, snake->head_row, snake->head_col, 'x');
    } else if (next == '*') {
      update_head(state, snum);
      add_food(state); // Generate a fruit in the board
    } else {
      update_head(state, snum);
      update_tail(state, snum);
    }
  }
  return;
}

/* Task 5 */
game_state_t* load_board(char* filename) {
  game_state_t *game = (game_state_t *) malloc(sizeof(game_state_t));

  unsigned int count_of_rows = count_number_of_rows(filename);
  game->num_rows = count_of_rows;
  char **b = (char **) malloc(sizeof(char *) * count_of_rows);
  game->board = b;
  
  FILE *fptr = fopen(filename, "r");
  FILE *fptr_1 = fopen(filename, "r");
  FILE *fptr_2 = fopen(filename, "r");

  for (int i = 0; i < count_of_rows; i++) {

    int char_count = count_number_of_char(fptr_1);
    unsigned int char_count_unsigned = count_number_of_char_unsigned(fptr_2);
    *b = (char *) malloc(sizeof(char) * (char_count_unsigned + 1));

    char *str = (char *) malloc(sizeof(char) * (char_count_unsigned + 1));
    fgets(str, char_count + 2, fptr);
    throw_enter(str);
    strcpy(*b, str);
    b += 1;
  }

  return game;
}

void throw_enter(char *str) {
  char *p = str;
  while (*p != '\n') {
    p += 1;
  }
  *p = '\0';
}

/* Count number of rows on a file */
unsigned int count_number_of_rows(char *filename) {
  unsigned int length = 0;
  FILE *fp1 = fopen(filename, "r");
  int c = fgetc(fp1);

  while (c != EOF) {
    if (c == '\n') {
      length ++;
    }
    c = fgetc(fp1);
  }
  fclose(fp1);
  return length;
}

/* Count number of characters on one line */
int count_number_of_char(FILE *fptr) {
  int count = 0;
  while (fgetc(fptr) != '\n') {
    count += 1;
  }
  return count;
}

unsigned int count_number_of_char_unsigned(FILE *fptr) {
  unsigned int count = 0;
  while (fgetc(fptr) != '\n') {
    count += 1;
  }
  return count;
}

/*
  Task 6.1

  Helper function for initialize_snakes.
  Given a snake struct with the tail row and col filled in,
  trace through the board to find the head row and col, and
  fill in the head row and col in the struct.
*/
static void find_head(game_state_t* state, unsigned int snum) {
  snake_t *snake = find_snake(state, snum);

  unsigned int row = snake->tail_row;
  unsigned int col = snake->tail_col;
  char c = get_board_at(state, row, col);
  while (!is_head(c)) {
    row = get_next_row(row, c);
    col = get_next_col(col, c);
    c = get_board_at(state, row, col);
  }
  snake->head_row = row;
  snake->head_col = col;
  return;
}

/* Find the snumth snake */
snake_t* find_snake(game_state_t *state, unsigned int snum) {
  snake_t *p = state->snakes;
  for (unsigned int i = 0; i < snum; i++) {
    p += 1;
  }
  return p;
}

/* Task 6.2 */
game_state_t* initialize_snakes(game_state_t* state) {
  unsigned int snum = count_snakes(state);
  state->num_snakes = snum;
  fill_in_tail(state, snum);
  for (unsigned int n = 0; n < snum; n++) {
    find_head(state, n);
  }
  return state;
}

/* Count number of snakes in the entire board*/
unsigned int count_snakes(game_state_t* state) {
  unsigned int count = 0;
  char **b = state->board;

  for (unsigned int row = 0; row < state->num_rows; row++) {
    for (unsigned int col = 0; col < strlen(*b); col++) {
      if (is_tail(get_board_at(state, row, col))) {
        count += 1;
      }
    }
    b += 1;
  }
  return count;
}

/* Fill in the tail row and col in array of snakes */
void fill_in_tail(game_state_t* state, unsigned int snum) {
  char **b = state->board;
  state->snakes = (snake_t *) malloc(sizeof(snake_t) * snum);
  snake_t *p = state->snakes;
  
  for (unsigned int row = 0; row < state->num_rows; row++) {
    for (unsigned int col = 0; col < strlen(*b); col++) {
      if (is_tail(get_board_at(state, row, col))) {
        p->tail_row = row;
        p->tail_col = col;
        p += 1;
      }
    }
    b += 1;
  }
}