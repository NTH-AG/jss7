/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.TransferredVolumeRollOverImpl;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ApplyChargingReportGPRSRequestTest {
	
	public byte[] getData() {
		return new byte[] { 48, 57, -96, 5, -127, 3, -128, 1, 24, -95, 35, -96,
				5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128, 3, 4,
				7, 7, -93, 3, -128, 1, 52, -92, 3, -128, 1, 53, -91, 3, -128,
				1, 54, -126, 1, -1, -125, 1, 2, -92, 5, -128, 3, -128, 1, 25 };
	};

	public byte[] getData2() {
		return new byte[] { 48, 57, -96, 5, -128, 3, -128, 1, 25, -95, 35, -96,
				5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128, 3, 4,
				7, 7, -93, 3, -128, 1, 52, -92, 3, -128, 1, 53, -91, 3, -128,
				1, 54, -126, 1, -1, -125, 1, 2, -92, 5, -127, 3, -128, 1, 24 };
	};
	
	private byte[] getEncodedqos2Subscribed1() {
		return new byte[] { 52 };
	}
	
	private byte[] getEncodedqos2Subscribed2() {
		return new byte[] { 53 };
	}
	
	private byte[] getEncodedqos2Subscribed3() {
		return new byte[] { 54 };
	}
	
	public byte[] getQoSSubscribedData(){
		return new byte[] { 4, 7, 7 };
	};
	
	public byte[] getExtQoSSubscribedData(){
		return new byte[] { 1, 7 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		ApplyChargingReportGPRSRequestImpl prim = new ApplyChargingReportGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		assertEquals(prim.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(),24);
		assertNull(prim.getChargingResult().getTransferredVolume());
		assertNotNull(prim.getQualityOfService());
		assertTrue(prim.getActive());
		assertEquals(prim.getPDPID().getId(),2);
		assertEquals(prim.getChargingRollOver().getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch().longValue(),25);
		assertNull(prim.getChargingRollOver().getElapsedTimeRollOver());
		
		
		data = this.getData2();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new ApplyChargingReportGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		assertEquals(prim.getChargingResult().getTransferredVolume().getVolumeIfNoTariffSwitch().longValue(),25);
		assertNull(prim.getChargingResult().getElapsedTime());
		assertNotNull(prim.getQualityOfService());
		assertTrue(prim.getActive());
		assertEquals(prim.getPDPID().getId(),2);
		assertEquals(prim.getChargingRollOver().getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch().intValue(),24);
		assertNull(prim.getChargingRollOver().getTransferredVolumeRollOver());
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		//=Option 1
		//chargingResult
		ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(24));
		ChargingResult chargingResult= new ChargingResultImpl(elapsedTime);
		//qualityOfService
		QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
		GPRSQoS requestedQoS = new GPRSQoSImpl(qosSubscribed);
		ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
		GPRSQoS subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);
		GPRSQoS negotiatedQoS = new GPRSQoSImpl(qosSubscribed);
		Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed1());
		GPRSQoSExtension requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
		Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed2());
		GPRSQoSExtension subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);
		Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed3());
		GPRSQoSExtension negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);
		QualityOfService qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS,
				negotiatedQoS, requestedQoSExtension, subscribedQoSExtension, negotiatedQoSExtension);
		//active
		boolean active = true;
		//pdpID
		PDPID pdpID = new PDPIDImpl(2);
		//chargingRollOver
		TransferredVolumeRollOverImpl transferredVolumeRollOver = new TransferredVolumeRollOverImpl(new Integer(25));
		ChargingRollOver chargingRollOver = new ChargingRollOverImpl(transferredVolumeRollOver);
		ApplyChargingReportGPRSRequestImpl prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService,
				active, pdpID, chargingRollOver);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
		
		
		//=Option 2
		//chargingResult
		TransferredVolumeImpl transferredVolume = new TransferredVolumeImpl(new Long(25));
		chargingResult= new ChargingResultImpl(transferredVolume);
		//chargingRollOver
		ElapsedTimeRollOverImpl elapsedTimeRollOver = new ElapsedTimeRollOverImpl(new Integer(24));
		chargingRollOver = new ChargingRollOverImpl(elapsedTimeRollOver);
		prim = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService,
				active, pdpID, chargingRollOver);
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

	}
	
}
