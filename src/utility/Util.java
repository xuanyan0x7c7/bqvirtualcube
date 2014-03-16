package utility;

import java.util.Random;

public class Util {

	public static void sort(int[] index, double[] data) {
		sort(index, 0, index.length, data);
	}

	private static void sort(int[] index, int begin, int end, double[] data) {
		if (end - begin < 10) {
			insertionSort(index, begin, end, data);
		} else {
			quickSort(index, begin, end, data);
		}
	}

	private static void insertionSort(int[] index, int begin, int end,
			double[] data) {
		for (int i = begin + 1; i < end; ++i) {
			int k = index[i];
			double d = data[k];
			int j;
			for (j = i; j > begin; --j) {
				if (data[index[j - 1]] > d) {
					index[j] = index[j - 1];
				} else {
					break;
				}
			}
			index[j] = k;
		}
	}

	static Random rand = new Random();

	private static void quickSort(int[] index, int begin, int end, double[] data) {
		int k = begin - 1;
		int l = end;
		double d = data[index[rand.nextInt(end - begin) + begin]];
		while (true) {
			while (data[index[++k]] < d)
				;
			while (data[index[--l]] > d)
				;
			if (k < l) {
				int temp = index[k];
				index[k] = index[l];
				index[l] = temp;
			} else {
				sort(index, begin, l + 1, data);
				sort(index, l + 1, end, data);
				return;
			}
		}
	}

}
