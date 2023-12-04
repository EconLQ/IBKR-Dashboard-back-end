package com.liquidus.ibkrdasboardjee8.rest.dashboard;

import com.liquidus.ibkrdasboardjee8.rest.dashboard.util.CsvFileUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("/upload")
public class FileUploadResource {
    Logger logger = Logger.getLogger(FileUploadResource.class.getName());

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@MultipartForm MultipartFormDataInput data) {

        Map<String, List<InputPart>> formDataMap = data.getFormDataMap();
        InputPart filePart = formDataMap.get("file").get(0); // get original file

        try {
            InputStream fileInputStream = filePart.getBody(InputStream.class, null);
            // get name of the file from the payload
            String fileName = filePart.getHeaders().getFirst("Content-Disposition").split("filename=")[1];

            CsvFileUtils csvFileUtils = new CsvFileUtils();
            // save file to user.home and create dir /reports if not exist
            String uploadedFileLocation = csvFileUtils.saveFileToHome(fileName);

            // rewrite saved file with data from file input stream
            csvFileUtils.saveToFile(fileInputStream, uploadedFileLocation);

            // parse .CSV file and save data from the file to the respected tables
            csvFileUtils.parseCsvFile();
        } catch (IOException e) {
            logger.warning("Failed to read from the input: " + e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        // return 200
        return Response.ok().build();
    }
}
