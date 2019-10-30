package test.net.vortexdata.tsqpf;

import net.vortexdata.tsqpf.configs.ConfigMain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMainTest {

    @Test
    public void getNonExistentConfigValue() {
        ConfigMain configMain = new ConfigMain();
        String returnValue = configMain.getProperty("someKeyThatDoesNotExist");
    }

}