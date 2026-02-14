package fr.forty_two.numbers;

public class NumberWorker {
	public class IllegalNumberException extends IllegalArgumentException {
		
		public IllegalNumberException(int number) {
			super("" + number);
		}
	}

	public boolean isPrime(int number) {
		if (number <= 1) {
			throw new IllegalNumberException(number);
		}

		if (number == 2) {
			return true;
		}

		if (number % 2 == 0) {
			return false;
		}

		double sqrt = Math.sqrt(number);

		for (int i = 3; i <= sqrt; i+=2) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	public int digitsSum(int number) {
		long digits = number < 0 ? -(long)number : number;
		int sum = 0;
		while (digits > 0) {
			sum += digits % 10;
			digits /= 10;
		}
		return sum;
	}
}
