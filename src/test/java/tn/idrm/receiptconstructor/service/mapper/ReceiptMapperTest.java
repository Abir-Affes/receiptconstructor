package tn.idrm.receiptconstructor.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ReceiptMapperTest {

    private ReceiptMapper receiptMapper;

    @BeforeEach
    public void setUp() {
        receiptMapper = new ReceiptMapperImpl();
    }
}
