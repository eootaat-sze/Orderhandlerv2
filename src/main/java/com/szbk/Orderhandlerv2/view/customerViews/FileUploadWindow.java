package com.szbk.Orderhandlerv2.view.customerViews;

import java.io.OutputStream;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import org.springframework.util.FastByteArrayOutputStream;

public class FileUploadWindow extends Window implements Receiver, SucceededListener {
    private VerticalLayout mainLayout;
    private Upload fileUpload;
    private ProgressBar progressBar;

    private AddOrderView ui;

    private FastByteArrayOutputStream outputStream;

    public FileUploadWindow(AddOrderView ui) {
        this.ui = ui;

        setWidth(500, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
        setCaption("Fájl feltöltése");
        setModal(true);
        setResizable(false);
        setClosable(true);
        setDraggable(false);

        setupContent();
        setContent(mainLayout);
    }

    private void setupContent() {
        //File upload settings.
        fileUpload = new Upload("Fájl feltöltése", this);
        fileUpload.addSucceededListener(this);

        //Progressbar settings.
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(true);
        progressBar.setCaption("Feltöltés...");

        mainLayout = new VerticalLayout(fileUpload, progressBar);
        mainLayout.setComponentAlignment(fileUpload, Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(progressBar, Alignment.BOTTOM_CENTER);
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        progressBar.setVisible(true);
        return outputStream = new FastByteArrayOutputStream();        
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        progressBar.setVisible(false);
        ui.addOrderFromFileToGrid(outputStream.toString());
	}
}