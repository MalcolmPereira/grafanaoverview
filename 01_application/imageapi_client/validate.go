package main

import (
	"bytes"
	"crypto/tls"
	"io"
	"io/ioutil"
	"log"
	"mime/multipart"
	"net/http"
	"os"
	"strings"
	"time"

	"github.com/google/uuid"
)

func main() {

	if len(os.Args) != 4 {
		log.Fatalln("Valid Service URL, Client Name and Image  Required")
		panic("Valid Service URL, Client Name and Image  Required")
	}
	REQUEST_ID := uuid.New()
	SERVICE_URL := os.Args[1]
	CLIENT_NAME := os.Args[2]
	IMAGE := os.Args[3]
	log.Println("REQUEST-ID:", REQUEST_ID, "Service URL", SERVICE_URL)
	log.Println("REQUEST-ID:", REQUEST_ID, "Tenant Id ", CLIENT_NAME)
	log.Println("REQUEST-ID:", REQUEST_ID, "Image Name ", IMAGE)

	//Allow for self signed certificates
	transCfg := &http.Transport{
		TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
	}

	//Get HTTP Client
	client := &http.Client{
		Timeout:   time.Second * 10,
		Transport: transCfg,
	}

	// New multipart writer.
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)

	fw, err := writer.CreateFormField("tenantId")
	if err != nil {
		panic(err)
	}
	_, err = io.Copy(fw, strings.NewReader(CLIENT_NAME))
	if err != nil {
		panic(err)
	}

	fw, err = writer.CreateFormFile("image", IMAGE)
	if err != nil {
		panic(err)
	}
	file, err := os.Open(IMAGE)
	if err != nil {
		panic(err)
	}
	_, err = io.Copy(fw, file)
	if err != nil {
		panic(err)
	}
	// Close multipart writer.
	writer.Close()
	req, err := http.NewRequest("POST", SERVICE_URL, bytes.NewReader(body.Bytes()))
	if err != nil {
		panic(err)
	}

	req.Header.Set("Content-Type", writer.FormDataContentType())
	req.Header.Set("REQUEST-ID", REQUEST_ID.String())
	rsp, _ := client.Do(req)
	if rsp == nil || rsp.StatusCode != http.StatusOK {
		//fmt.Println("Invalid Service Response, Service Not Available ")
		log.Fatalln("REQUEST-ID:", REQUEST_ID, "Invalid Service Response, Service Not Available ")
	} else {
		//fmt.Println("Got Response Trace ID ", rsp.Header.Get("TRACE_ID"))
		log.Println("REQUEST-ID:", REQUEST_ID, "Got TRACE-ID ", rsp.Header.Get("TRACE_ID"))
		body, err := ioutil.ReadAll(rsp.Body)
		if err != nil {
			panic(err)
		} else {
			//fmt.Println("Got Response Body ", string(body[:]))
			log.Println("REQUEST-ID:", REQUEST_ID, "Got Response Body ", string(body[:]))
		}
	}
}
