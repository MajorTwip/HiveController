package ch.comstock.hivecontroller.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MHPSjsonTest {

	@Test
	void test() {
		MHPSjson.getConsumption("http://10.87.30.150");
	}

}
