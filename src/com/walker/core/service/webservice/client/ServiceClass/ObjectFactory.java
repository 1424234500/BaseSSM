
package com.walker.core.service.webservice.client.ServiceClass;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the util.service.webservice.client.ServiceClass package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoClassMethodResponse_QNAME = new QName("http://serviceImpl.service.util/", "doClassMethodResponse");
    private final static QName _Test_QNAME = new QName("http://serviceImpl.service.util/", "test");
    private final static QName _TestResponse_QNAME = new QName("http://serviceImpl.service.util/", "testResponse");
    private final static QName _DoClassMethod_QNAME = new QName("http://serviceImpl.service.util/", "doClassMethod");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: util.service.webservice.client.ServiceClass
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DoClassMethodResponse }
     * 
     */
    public DoClassMethodResponse createDoClassMethodResponse() {
        return new DoClassMethodResponse();
    }

    /**
     * Create an instance of {@link Test }
     * 
     */
    public Test createTest() {
        return new Test();
    }

    /**
     * Create an instance of {@link TestResponse }
     * 
     */
    public TestResponse createTestResponse() {
        return new TestResponse();
    }

    /**
     * Create an instance of {@link DoClassMethod }
     * 
     */
    public DoClassMethod createDoClassMethod() {
        return new DoClassMethod();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoClassMethodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://serviceImpl.service.util/", name = "doClassMethodResponse")
    public JAXBElement<DoClassMethodResponse> createDoClassMethodResponse(DoClassMethodResponse value) {
        return new JAXBElement<DoClassMethodResponse>(_DoClassMethodResponse_QNAME, DoClassMethodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Test }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://serviceImpl.service.util/", name = "test")
    public JAXBElement<Test> createTest(Test value) {
        return new JAXBElement<Test>(_Test_QNAME, Test.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://serviceImpl.service.util/", name = "testResponse")
    public JAXBElement<TestResponse> createTestResponse(TestResponse value) {
        return new JAXBElement<TestResponse>(_TestResponse_QNAME, TestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoClassMethod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://serviceImpl.service.util/", name = "doClassMethod")
    public JAXBElement<DoClassMethod> createDoClassMethod(DoClassMethod value) {
        return new JAXBElement<DoClassMethod>(_DoClassMethod_QNAME, DoClassMethod.class, null, value);
    }

}
