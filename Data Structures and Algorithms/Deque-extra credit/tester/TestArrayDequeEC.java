package tester;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void TestStudentsArray() {
        StudentArrayDeque<Integer> studArray = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> corrArray = new ArrayDequeSolution<>();
        String message = "";

        for (int times = 50000; times > 0; times -= 1) {
            int n = StdRandom.uniform(4);
            int number = StdRandom.uniform(999);

            if (n == 0) {
                studArray.addFirst(number);
                corrArray.addFirst(number);
                message = message + "addFirst(" + number + ")\n";
                assertEquals(message, corrArray.get(0), studArray.get(0));
            } else if (n == 1) {
                studArray.addLast(number);
                corrArray.addLast(number);
                int studSize = studArray.size();
                int corrSize = corrArray.size();
                message = message + "addLast(" + number + ")\n";
                assertEquals(message, corrArray.get(corrSize - 1), studArray.get(studSize - 1));
            } else if (n == 2 && (!studArray.isEmpty() && !corrArray.isEmpty())) {
                    Integer stuItem = studArray.removeFirst();
                    Integer corrItem = corrArray.removeFirst();
                    message = message + "removeFirst()\n";
                    assertEquals(message, corrItem, stuItem);
                    assertEquals(message, corrArray.size(), studArray.size());
            } else if (n == 3 && (!studArray.isEmpty() && !corrArray.isEmpty())) {
                Integer stuItem = studArray.removeLast();
                Integer corrItem = corrArray.removeLast();
                message = message + "removeLast()\n";
                assertEquals(message, corrItem, stuItem);
                assertEquals(message, corrArray.size(), studArray.size());
            }
        }
    }
}
