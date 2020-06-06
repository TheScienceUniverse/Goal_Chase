package dev.the01guy.goal_chase.utility;

class Hash {
	private String H[] = new String [8];
	private String W[] = new String [64];

	private String _K[] = {
			"01000010100010100010111110011000", "01110001001101110100010010010001", "10110101110000001111101111001111", "11101001101101011101101110100101",
			"00111001010101101100001001011011", "01011001111100010001000111110001", "10010010001111111000001010100100", "10101011000111000101111011010101",
			"11011000000001111010101010011000", "00010010100000110101101100000001", "00100100001100011000010110111110", "01010101000011000111110111000011",
			"01110010101111100101110101110100", "10000000110111101011000111111110", "10011011110111000000011010100111", "11000001100110111111000101110100",
			"11100100100110110110100111000001", "11101111101111100100011110000110", "00001111110000011001110111000110", "00100100000011001010000111001100",
			"00101101111010010010110001101111", "01001010011101001000010010101010", "01011100101100001010100111011100", "01110110111110011000100011011010",
			"10011000001111100101000101010010", "10101000001100011100011001101101", "10110000000000110010011111001000", "10111111010110010111111111000111",
			"11000110111000000000101111110011", "11010101101001111001000101000111", "00000110110010100110001101010001", "00010100001010010010100101100111",
			"00100111101101110000101010000101", "00101110000110110010000100111000", "01001101001011000110110111111100", "01010011001110000000110100010011",
			"01100101000010100111001101010100", "01110110011010100000101010111011", "10000001110000101100100100101110", "10010010011100100010110010000101",
			"10100010101111111110100010100001", "10101000000110100110011001001011", "11000010010010111000101101110000", "11000111011011000101000110100011",
			"11010001100100101110100000011001", "11010110100110010000011000100100", "11110100000011100011010110000101", "00010000011010101010000001110000",
			"00011001101001001100000100010110", "00011110001101110110110000001000", "00100111010010000111011101001100", "00110100101100001011110010110101",
			"00111001000111000000110010110011", "01001110110110001010101001001010", "01011011100111001100101001001111", "01101000001011100110111111110011",
			"01110100100011111000001011101110", "01111000101001010110001101101111", "10000100110010000111100000010100", "10001100110001110000001000001000",
			"10010000101111101111111111111010", "10100100010100000110110011101011", "10111110111110011010001111110111", "11000110011100010111100011110010"
	};

	private String _H[] = {
			"01101010000010011110011001100111",
			"10111011011001111010111010000101",
			"00111100011011101111001101110010",
			"10100101010011111111010100111010",
			"01010001000011100101001001111111",
			"10011011000001010110100010001100",
			"00011111100000111101100110101011",
			"01011011111000001100110100011001"
	};

	String sha256 (String input) {
		String output = "";
		input = prepareInputBits (input);
		int i, n = (int) Math.floor (input.length() / 512);
		prepareData();

		for (i = 0; i < input.length(); i += 512) { // (n * 512 bits) chunk
			hash (input.substring (i, i + 512));
		}

		for (i = 0; i < H.length; i++) {
			output += H[i];
		}

		output = convertBase2To16 (output);

		return output;
	}

	private void hash (String input) { // 512 bits in
		int i;
		String sigma0 = new String ("");
		String sigma1 = new String ("");

		/*
		 ** 64 rounds = (16 + 48) rounds
		 ** for first 16 rounds, 512 bits = 16 * 32 bits = 16 words
		 ** for next 48 rounds, calculate round word, W[i]
		 */

		for (i = 0; i < 64; i++) {
			if (i < 16) {
				W[i] = input.substring (32 * i, 32 * i + 32);
			} else {
				sigma0 = bitsXor (bitsXor (rotateRight (W[i-15], 7), rotateRight (W[i-15], 18)), shiftRight (W[i-15], 3));
				sigma1 = bitsXor (bitsXor (rotateRight (W[i-2], 17), rotateRight (W[i-2], 19)), shiftRight (W[i-2], 10));
				W[i] = bitsAdd (W[i-16], W[i-7]);
				W[i] = bitsAdd (W[i], bitsAdd (sigma0, sigma1));
			}
		}

		String t0 = new String ("");
		String t1 = new String ("");

		for (int round = 0; round < 64; round++) {
			t0 = bitsAdd (H[7], bitsAdd (bitsAdd (Ep1(H[4]), Ch(H[4], H[5], H[6])), bitsAdd (_K[round], W[round])));
			t1 = bitsAdd (Ep0(H[0]), Maj(H[0], H[1], H[2]));

			H[7] = H[6];
			H[6] = H[5];
			H[5] = H[4];
			H[4] = bitsAdd (H[3], t0);
			H[3] = H[2];
			H[2] = H[1];
			H[1] = H[0];
			H[0] = bitsAdd (t0, t1);
		}
	}

	private String prepareInputBits (String input) {
		// binary-message + padding (1000...) + message-length (64 bits) => 0 (modulo 512)
		String output = new String ("");
		int i, n;
		String x;

		// converting character ASCII or UTF to hex-number (16 bits each) string
		for (i = 0; i < input.length(); i++) {
			x = convertBase10To2 (input.codePointAt(i));
			output += padLeftZeroes (x, (16 - x.length()));
		}

		// getting 64 bit message length
		String msgLen = convertBase10To2 (output.length());
		msgLen = padLeftZeroes (msgLen, 64 - msgLen.length());

		n = output.length() % 512; // getting extra bits length
		int nP = 0; // number of zeroes in pad

		if (n == 0) {
			// add new block
			nP = 512 - 65; // 65 = 64 + 1
		} else if (n > 447) {
			// add new block
			nP = (512 - n) + (512 - 65);
		} else {
			// use last block
			nP = 512 - n - 65;
		}

		output = output + "1" + padLeftZeroes ("", nP) + msgLen;

		return output;
	}

	private void prepareData() {
		int i;

		// initialize hash value with base hash constants
		// H = new String [8];
		for (i = 0; i < 8; i++) {
			H[i] = _H[i];
		}

		// initialize word with 64 empty string
		// W = [];
		for (i = 0; i < 64; i++) {
			W[i] = "";
		}
	}

	// *** basic hash functions ***
	private String Ch (String E, String F, String G) {
		return bitsXor (bitsAnd (E, F), bitsAnd (bitsNot (E), G));
	}

	private String Maj (String A, String B, String C) {
		return bitsXor (bitsXor (bitsAnd (A, B), bitsAnd (B, C)), bitsAnd (C, A));
	}

	private String Ep0 (String A) {
		return bitsXor (bitsXor (rotateRight(A, 2), rotateRight(A, 13)), rotateRight(A, 22));
	}

	private String Ep1 (String E) {
		return bitsXor (bitsXor (rotateRight(E, 6), rotateRight(E, 11)), rotateRight(E, 25));
	}

	// *** basic binary functions ***
	private String padLeftZeroes (String input, int numberOfZeroes) {
		String initialZeroes = "";

		for (int i = 0; i < numberOfZeroes; i++) {
			initialZeroes += "0";
		}

		return (initialZeroes + input);
	}

	private String convertBase10To2 (int input) {
		String output = "";

		while (input != 0) {
			output = (input % 2) + output;
			input = (int) Math.floor(input / 2);
		}

		return output;
	}

	private int convertBase2To10 (String base2s) {
		int base10n = 0;

		for (int i = 0; i < base2s.length(); i++) {
			base10n = 2 * base10n + ((base2s.charAt(i) == '1') ? 1 : 0);
		}

		return base10n;
	}

	private String convertBase2To16 (String input) {
		String listNibbles[] = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
		char listDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		String output = "";
		int i, j;
		int x = (input.length() % 4);

		if (x != 0) {
			input = padLeftZeroes (input, 4 - x);
		}

		for (i = 0; i < input.length(); i += 4) {
			for (j = 0; j < listDigits.length; j++) {
				if (input.substring (i, i + 4).equals(listNibbles[j])) {
					break;
				}
			}

			output += listDigits[j];
		}

		return output;
	}

	private String convertToNibbles (String input) {
		String output = "";

		for (int i = 0; i < input.length(); i += 4) {
			output += getNibble (input.substring (i, i + 4));
		}

		return output;
	}

	private char getNibble (String input) {
		char list[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		return list [convertBase2To10 (input)];
	}

	// *** basic bits functions ***
	private String shiftRight (String input, int numberOfBits) {
		String output = "";
		output = input.substring (0, numberOfBits);
		output = padLeftZeroes (output, input.length() - numberOfBits);
		return output;
	}

	private String rotateLeft (String input, int numberOfBits) {
		String output = "";
		output = input.substring (numberOfBits, input.length()) + input.substring (0, numberOfBits);
		return output;
	}

	private String rotateRight (String input, int numberOfBits) {
		String output = "";
		output = input.substring(input.length() - numberOfBits, input.length()) + input.substring(0, input.length() - numberOfBits);
		return output;
	}

	// *** basic gate functions ***
	private char bitNot (char x) {
		return (x == '0') ? '1' : '0';
	}

	private char bitOr (char x, char y) {
		return (x == '1' || y == '1') ? '1' : '0';
	}

	private char bitXor (char x, char y) {
		return (x == y) ? '0' : '1';
	}

	private char bitAnd (char x, char y) {
		return (x == '0' || y == '0') ? '0' : '1';
	}

	// *** bunch gates functions ***
	private String bitsOr (String a, String b) {
		String c = "";

		if (a.length() != b.length()) {
			a = padLeftZeroes (a, b.length() - a.length());
			b = padLeftZeroes (b, a.length() - b.length());
		}

		for (int i = 0; i < a.length(); i++) {
			c += bitOr (a.charAt(i), b.charAt(i));
		}

		return c;
	}

	private String bitsAnd (String a, String b) {
		String c = "";

		if (a.length() != b.length()) {
			a = padLeftZeroes(a, b.length() - a.length());
			b = padLeftZeroes(b, a.length() - b.length());
		}

		for (int i = 0; i < a.length(); i++) {
			c += bitAnd (a.charAt(i), b.charAt(i));
		}

		return c;
	}

	private String bitsNot (String a) {
		String b = "";

		for (int i = 0; i < a.length(); i++) {
			b += bitNot (a.charAt(i));
		}

		return b;
	}

	private String bitsXor (String a, String b) {
		String c = "";

		if (a.length() != b.length()) {
			a = padLeftZeroes(a, b.length() - a.length());
			b = padLeftZeroes(b, a.length() - b.length());
		}

		for (int i = 0; i < a.length(); i++) {
			c += bitXor (a.charAt(i), b.charAt(i));
		}

		return c;
	}

	// basic arithmetic functions
	private String bitAdd (char c_in, char x, char y) {
		char xor = bitXor (x, y);
		char c_out = bitOr (bitAnd (x, y), bitAnd (c_in, xor));
		char s = bitXor (c_in, xor);
		return ("" + c_out + s);
	}

	private String bitsAdd (String a, String b) {
		int i = 0;
		String s = "";
		char c = '0';
		String x = "";

		if (a.length() != b.length()) {
			a = padLeftZeroes (a, b.length() - a.length());
			b = padLeftZeroes (b, a.length() - b.length());
		}

		for (i = a.length() - 1; i >= 0; i--) {
			x = bitAdd (c, a.charAt(i), b.charAt(i));
			c = x.charAt(0);
			s = x.charAt(1) + s;
		}

		s = c + s;

		return trimNumber (s, a.length());
	}

	private String trimNumber (String input, int bitsLength) {
		int x = input.length() - bitsLength;
		return input.substring (x, x + bitsLength);
	}
}