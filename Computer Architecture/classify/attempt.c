// Task 2: ReLu
int[] arr = {-6, -1, 6, 1};
for (int i = 0; i < length(arr); i++) {
    if (arr[0] < 0) {
        arr[0] = 0;
    }
}

return arr;

// Task 3: argmax
int[] arr = {-6, -1, 6, 1};
int max = arr[0];
int index = 0;
for (int i = 0; i < length(arr); i++) {
    if (max < arr[i]) {
        max = arr[i];
        index = i;
    }
}
return index

// Task 4: dot
int[] arr0 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
int[] arr1 = {6, 1, 6, 1, 6, 1, 6, 1, 6};
int num = 3;
int strike0 = 2;
int strike1 = 3;
int sum_of_products = 0;
for (int i = 0; i < num; i++) {
    sum_of_products += arr[i * strike0] * arr[i *strike1];
}
return sum_of_products;

// Matrix multiplications
int* arr1 = {1, 2, 4, 5, 7, 8};
int* arr2 = {1, 2, 3, 4, 5, 6, 7, 8};
int* arr3 = malloc(n*k, sizeof(int));
int* Matrix(arr1, m, k, arr2, n, m, arr3) {
    for (int i = 0; i < k; i++) { // arr3's each row == arr1's row
        for (int j = 0; j < n; j++) { // arr3's each column == arr2's column
            arr3[i*j] = dot(arr1, arr2, m, 1, n) // m is arr2's row
            *arr2 = *arr2 + 4;
        }   
    *arr1 = *arr1 + 4 * m;
}
return arr3;
}

// Task 7: Read Matrix
int* readMatrix(char* fileName, int* row, int* column) {
    int file = fopen(fileName, 0);

    if (file == -1) {
        return 0;
    }

    // int* buffer = malloc(2 * sizeof(int));

    // if (buffer == 0) {
    //     return 0;
    // }

    ////////////////////////// Above finished without testing

    // int r = fread(file, buffer, 8);

    // if (r != 8) {
    //     return 0;
    // }

    int r = fread(file, row, 4);
    if (r == 4) {
        return 0;
    }

    int r = fread(file, column, 4);
    if (r == 4) {
        return 0;
    }
    ///////////////////////// Above finished without testing

    int total = (*row) * (*column)
    int* matrix = malloc(total * sizeof(int));

    if (matrix == 0) {
        return 0;
    }

    
    int r = fread(file, matrix, sizeof(int) * total);
    if (r == sizeof(int) * total) {
        return 0;
    }


    int close = fclose(file);

    if (close == -1) {
        return 0;
    }

    return matrix;
}

// Task 8: Write Matrix
void writeMatrix(char* fileName, int* matrix, int row, int column) {
    int file = fopen(fileName, 1);

    if (file == -1) {
        return 0;
    }

    int *buffer = malloc(2 * sizeof(int));
    *buffer = row;
    *(buffer + 4) = column;
    int w = fwrite(file, buffer, 2, 4);
    if (w != 2) {
        return 0;
    }

    int total = row * column;
    int w = fwrite(file, matrix, total, 4);
    if (w != total) {
        return 0;
    }

    int close = fclose(file);
    if (close == -1) {
        return 0;
    }
}


// Task 9: Classify
int classify(int argc, char** argv, int set) {
    int* row_0 = malloc(sizeof(int));
    int* column_0 = malloc(sizeof(int));

    char* m0 = *(argv + 4);

    int* m0 = readMatrix(m0, row, column);

    
    
}