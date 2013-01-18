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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class GPRSEventTest {
	
	public byte[] getData() {
		return new byte[] { 48, 6, -128, 1, 2, -127, 1, 1};
	};
	
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		GPRSEventImpl prim = new GPRSEventImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		assertEquals(prim.getGPRSEventType(),GPRSEventType.attachChangeOfPosition);
		assertEquals(prim.getMonitorMode(),MonitorMode.notifyAndContinue);
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		GPRSEventImpl prim = new GPRSEventImpl(GPRSEventType.attachChangeOfPosition, MonitorMode.notifyAndContinue);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
	
}
