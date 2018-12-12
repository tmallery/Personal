package tgm.utils;

public class ThreadJob<T> {

	private T m_input;

	public void setInput(T input) {
		m_input = input;
	}

	public T getInput() {
		return m_input;
	}
}
