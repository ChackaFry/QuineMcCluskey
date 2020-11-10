public class QuineMcCluskey {

	public static final int BIN_MAX_LENGTH = 4;

	public static void main(String args[]) {
//		int[] input = { 14, 2, 5, 6, 8, 10, 0 };
//		int[] input = { 14, 1, 2, 5, 6, 7, 8, 9, 10, 0 };
		int[] input = { 1, 4, 6, 7, 8, 9, 10, 11, 15 };
//		Integer[] input = { 1, 3, 7, 11, 15 };
//		Integer[] dontCare = { 0, 5 };
//		int[] dontCare = {};
//		int[] total = new int[0];
//		int[] iTotal = new int[0];

//		for (int i = 0; i < dontCare.length; i++)
//			total = add(input, dontCare[i]);
//
//		for (int i = 0; i < total.length; i++)
//			iTotal[i] = total[i];
//
//		iTotal = bubbleSort(iTotal);
//		print(iTotal);

		String[][] groups = generateColumn1(input);

		String col1 = print(groups);

		// Find All Columns
		String[] columns = { col1 };
		while (columns[columns.length - 1].length() > 2)
			columns = add(columns, columnCalc(columns[columns.length - 1]));
		columns = removeRecord(columns, columns.length - 1);

//		for (int i = 0; i < columns.length; i++) 
//			System.out.println(columns[i]);

		String[] altCol = new String[columns.length];
		for (int i = 0; i < altCol.length; i++)
			altCol[i] = merge(columns[i].split("\n\n"), "\n");

//		for (int i = 0; i < altCol.length; i++)
//			System.out.println(altCol[i]);

		String[] finalSolutions = unusedRecords(altCol);

		String[] finalColumn = duplicateRecords(altCol[altCol.length - 1]).split("\n");

		for (int i = 0; i < finalColumn.length; i++) {
			String record = finalColumn[i];
			finalSolutions = add(finalSolutions, record);
		}
		String prIm = "";
		String[] prImArr = new String[0];
		String[] coords = new String[0];
		for (int i = 0; i < finalSolutions.length; i++) {
			String prIm1 = generatePrIm(finalSolutions[i].split(";")[1].trim());
			prImArr = add(prImArr, prIm1);
			coords = add(coords, finalSolutions[i].split(";")[0]);
			prIm += prIm1 + " + ";
		}
		prIm = prIm.substring(0, prIm.length() - 3);
		System.out.println(prIm);
		print(prImArr);

		for (int i = 0; i < coords.length; i++) {
			System.out.println(coords[i]);
		}

		// Prime Implicant Chart
		boolean[][] primeImp = new boolean[coords.length][input.length];

		// Putting 'X' in the chart
		for (int i = 0; i < primeImp.length; i++) {
			int[] numbers = toIntArr(coords[i].split(","));
			for (int j = 0; j < numbers.length; j++)
				for (int k = 0; k < input.length; k++)
					if (numbers[j] == input[k])
						primeImp[i][k] = true;
		}

		for (int i = 0; i < primeImp.length; i++) {
			for (int j = 0; j < primeImp[i].length; j++) {
				System.out.print(primeImp[i][j] + "\t");
			}
			System.out.println();
		}

		String essentialPI = "";
		String ePI = "";
		for (int i = 0; i < primeImp[0].length; i++) {
			int counter = 0;
			for (int j = 0; j < primeImp.length; j++) {
				if (primeImp[j][i] == true)
					counter++;
			}
//			System.out.println(" " + counter);

			if (counter == 1)
				for (int j = 0; j < primeImp.length; j++)
					if (primeImp[j][i] == true) {
						essentialPI += prImArr[j] + ", ";
						ePI += coords[j] + ", ";
					}
		}

		essentialPI = essentialPI.substring(0, essentialPI.length() - 2);
		ePI = ePI.substring(0, ePI.length() - 2);
		System.out.println(essentialPI);
		String[] essentialPIArr = essentialPI.split(",");

		for (int i = 0; i < essentialPIArr.length; i++) {
			essentialPIArr[i] = essentialPIArr[i].trim();
		}

		for (int i = 0; i < essentialPIArr.length; i++) {
			for (int j = 0; j < essentialPIArr.length; j++) {
				if (i == j)
					continue;
				if(essentialPIArr[i].equals(essentialPIArr[j])) {
					essentialPIArr = removeRecord(essentialPIArr, j);
					j--;
					i--;
				}
			}
		}
		
		essentialPI = merge(essentialPIArr, ", ");
		System.out.println(essentialPI);

//		System.out.println(ePI);

		int[] ePIRepresented = toIntArr(ePI.split(","));
		print(ePIRepresented);

		int[] missing = new int[input.length];
		for (int i = 0; i < input.length; i++)
			missing[i] = input[i];

		for (int i = 0; i < ePIRepresented.length; i++) {
			for (int j = 0; j < missing.length; j++) {
				if (ePIRepresented[i] == missing[j]) {
					missing = removeRecord(missing, j);
					j--;
				}
			}
		}

		print(missing);

		double[] points = new double[coords.length];
		for (int i = 0; i < coords.length; i++) {
			int[] co = toIntArr(coords[i].split(","));
			int counter = 0;
			for (int j = 0; j < co.length; j++) {
				for (int k = 0; k < missing.length; k++) {
					if (co[j] == missing[k]) {
						counter++;
					}
				}
			}
			points[i] = (double) counter / (co.length + missing.length);
		}

		for (int i = 0; i < points.length; i++) {
			System.out.print(points[i] + " ");
		}

		System.out.println();

		double smallest = 0;
		int address = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i] > smallest) {
				smallest = points[i];
				address = i;
			}
		}

		essentialPI += ", " + prImArr[address];

		System.out.println(essentialPI);

	}

	private static String generatePrIm(String str) {
		String primeImp = "";
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == '0')
				primeImp += Character.toString('A' + i) + "'";
			else if (str.charAt(i) == '1')
				primeImp += Character.toString('A' + i);

		return primeImp;
	}

	private static String[] unusedRecords(String[] column) {
		String[] finalSolutions = new String[0];
		for (int i = 0; i < column.length - 1; i++) {
			String[] current = column[i].split("\n");
			String[] next = column[i + 1].split("\n");

			for (int j = 0; j < current.length; j++) {
				boolean change = false;
				int matchLen = current[j].split(";")[0].split(",").length;
				String matchThis = current[j].split(";")[0].trim();
				String[] compare = new String[0];

				for (int k = 0; k < next.length; k++) {
					compare = next[k].split(";")[0].split(",");
					compare = mergeN(compare, matchLen);

					for (int l = 0; l < compare.length; l++) {
//						System.out.println(matchThis + " " + compare[l]);
						if (matchThis.equals(compare[l])) {
//							System.out.println(matchThis + " == " + compare[l]);
							if (!change) {
								current = removeRecord(current, j);
								change = true;
								j--;
							}
						}
					}
				}

			}
			for (int j = 0; j < current.length; j++) {
				String record = current[j];
				finalSolutions = add(finalSolutions, record);
			}
		}

		return finalSolutions;
	}

	private static String[] mergeN(String[] str, int matchLen) {
		String[] newArr = new String[str.length / matchLen];

//		print(str);

		int j = 0;
		while (j < newArr.length) {
			newArr[j] = "";
			for (int i = j * matchLen; i < (j * matchLen) + matchLen; i++) {
				newArr[j] += str[i].trim() + ", ";
			}
			newArr[j] = newArr[j].substring(0, newArr[j].length() - 2).trim();
//			System.out.println(newArr[j]);
			j++;
		}

		return newArr;
	}

	private static String[][] generateColumn1(int[] input) {
		String[] binInput = new String[input.length];
		String[][] groups = new String[BIN_MAX_LENGTH + 1][];

		for (int i = 0; i < input.length; i++)
			binInput[i] = String.format("%" + BIN_MAX_LENGTH + "s", Integer.toBinaryString(input[i])).replace(' ', '0');

		print(binInput);

		for (int i = 0; i < binInput.length; i++) {
			int counter = 0;
			for (int j = 0; j < binInput[i].length(); j++)
				if (binInput[i].charAt(j) == '1')
					counter++;
			groups[counter] = add(groups[counter], binInput[i]);
		}

		boolean emptyArr = true;
		int i = 0;
		while (emptyArr) {
			try {
				while (i < groups.length) {
					String[] group = groups[i];
					if (group.length == 0)
						System.out.println();
					i++;
				}
				emptyArr = false;
			} catch (Exception e) {
				groups = removeRecord(groups, i);
				emptyArr = true;
				i = 0;
			}
		}

		return groups;
	}

	private static String columnCalc(String column) {
		String[] groups = column.split("\n\n");

		// Generate Next Column
		String x = "";
		for (int i = 0; i < groups.length - 1; i++) {
			String[] current = groups[i].split("\n");
			String[] next = groups[i + 1].split("\n");
			String y = "";

			for (int j = 0; j < current.length; j++)
				for (int k = 0; k < next.length; k++) {
					String[] val1 = current[j].split(";");
					String[] val2 = next[k].split(";");
					String comparison = implicantComparison(val1[1], val2[1]);
					if (comparison.charAt(0) == 't') {
						comparison = comparison.substring(1);
						y += val1[0] + ", " + val2[0] + "; " + comparison + "\n";
					}
				}

			x += y + "\n";
		}

		return x;
	}

	private static String duplicateRecords(String str) {
		String[] groups = str.split("\n\n");

		for (int i = 0; i < groups.length; i++) {
			String[] current = groups[i].split("\n");
			for (int j = 0; j < current.length; j++) {
				String[] val1 = current[j].split(";");
				int[] arr1 = toIntArr(val1[0].split(","));

				for (int k = j + 1; k < current.length; k++) {
					String[] val2 = current[k].split(";");
					int[] arr2 = toIntArr(val2[0].split(","));
					if (intArrEquals(arr1, arr2)) {
						current = removeRecord(current, k);
						k--;
					}
				}
			}
			groups[i] = merge(current, "\n");
		}

		str = merge(groups, "\n");

		return str;
	}

	private static String implicantComparison(String current, String next) {
		current = current.trim();
		next = next.trim();

		int counter = 0;
		String x = "";
		for (int i = 0; i < current.length(); i++)
			if (current.charAt(i) != next.charAt(i)) {
				counter++;
				x += "_";
			} else
				x += current.charAt(i);

		if (counter == 1)
			return "t" + x;
		else
			return "f" + x;
	}

	private static int[] toIntArr(String[] strs) {
		int[] arr = new int[strs.length];
		for (int i = 0; i < strs.length; i++)
			arr[i] = Integer.parseInt(strs[i].trim());
		return bubbleSort(arr);
	}

	private static boolean intArrEquals(int[] arr1, int[] arr2) {
		boolean x = true;
		for (int i = 0; i < arr1.length; i++)
			x = x && (arr1[i] == arr2[i]);

		return x;
	}

	private static int[] removeRecord(int[] ints, int i) {
		try {
			if (ints.length - 1 == 0)
				return new int[0];

			int[] arr = new int[ints.length - 1];
			for (int j = 0; j < i; j++)
				arr[j] = ints[j];
			for (int j = i; j < arr.length; j++)
				arr[j] = ints[j + 1];
			return arr;
		} catch (Exception e) {
			return new int[0];
		}
	}

	private static String[] removeRecord(String[] strs, int i) {
		try {
			if (strs.length - 1 == 0)
				return new String[0];

			String[] arr = new String[strs.length - 1];
			for (int j = 0; j < i; j++)
				arr[j] = strs[j];
			for (int j = i; j < arr.length; j++)
				arr[j] = strs[j + 1];
			return arr;
		} catch (Exception e) {
			return new String[0];
		}
	}

	private static String[][] removeRecord(String[][] strs, int i) {
		try {
			if (strs.length - 1 == 0)
				return new String[0][];

			String[][] arr = new String[strs.length - 1][];
			for (int j = 0; j < i; j++)
				arr[j] = strs[j];
			for (int j = i; j < arr.length; j++)
				arr[j] = strs[j + 1];
			return arr;
		} catch (Exception e) {
			return new String[0][];
		}
	}

	private static String merge(String[] strs, String arg) {
		String str = "";
		for (int i = 0; i < strs.length; i++)
			str += strs[i] + arg;
		return str.substring(0, str.length()-arg.length());
	}

	private static int[] bubbleSort(int[] input) {
		int i = input.length;
		while (i > 0) {
			for (int j = 0; j < input.length - 1; j++)
				if (input[j] > input[j + 1]) {
					int swap = input[j];
					input[j] = input[j + 1];
					input[j + 1] = swap;
				}
			i--;
		}

		return input;
	}

	private static String[] add(String[] arr, String str) {
		try {
			String[] a = new String[arr.length + 1];
			for (int i = 0; i < arr.length; i++)
				a[i] = arr[i];
			a[a.length - 1] = str;
			return a;
		} catch (Exception e) {
			String[] a = new String[1];
			a[0] = str;
			return a;
		}
	}

//	private static int[] add(int[] arr, int str) {
//		try {
//			int[] a = (int[]) new int[arr.length + 1];
//			for (int i = 0; i < arr.length; i++)
//				a[i] = arr[i];
//			a[a.length - 1] = str;
//			return a;
//		} catch (Exception e) {
//			int[] a = (int[]) new int[1];
//			a[0] = str;
//			return a;
//		}
//	}

	private static String print(String[][] strs) {
		int dim1 = strs.length;

		String y = "";
		for (int i = 0; i < dim1; i++) {
			String[] current = strs[i];
			int dim2 = current.length;
			String x = "";
			for (int j = 0; j < dim2; j++) {
				int val = Integer.parseInt(current[j], 2);
				x += val + "; " + current[j] + "\n";
			}
			y += x + "\n";
		}
		y = y.substring(0, y.length() - 1);

//		System.out.println(y);
		return y;
	}

	private static String print(String[] y) {
		String x = "";
		for (int i = 0; i < y.length; i++)
			x += y[i] + ", ";

		x = x.substring(0, x.length() - 2);
		System.out.println(x);
		return x;
	}

	private static void print(int[] input) {
		for (int i = 0; i < input.length; i++)
			System.out.print(input[i] + " ");
		System.out.println();
	}

}
