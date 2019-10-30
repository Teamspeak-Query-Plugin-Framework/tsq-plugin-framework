package test.net.vortexdata.tsqpf;

import net.vortexdata.tsqpf.configs.ConfigMain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMainTest {

    @Test
    public void getNonExistantConfigValue() {
        ConfigMain configMain = new ConfigMain();
    }

}