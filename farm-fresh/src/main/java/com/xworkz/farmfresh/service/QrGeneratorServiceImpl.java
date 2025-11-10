package com.xworkz.farmfresh.service;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class QrGeneratorServiceImpl implements QrGeneratorService {

    @Value("${qr.code.directory}")
    private String qrCodeDirectory;

    public QrGeneratorServiceImpl()
    {
        log.info("QrGeneratorServiceImpl constructor");
    }

    @Override
    public String generateSupplierQR(Integer supplierId, String email, String phoneNumber) {
        log.info("generateSupplierQR method in QrGeneratorServiceImpl");
        try {

            String qrData = "ID:" + supplierId + ";EMAIL:" + email + ";PHONE:" + phoneNumber;
            String filePath = qrCodeDirectory + "supplier_" + supplierId + ".png";

            BitMatrix matrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 250, 250);
            Path path = Paths.get(filePath);
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            return filePath;
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }
}
