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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class TransferredVolumeRollOverTest {
	
	public byte[] getData() {
		return new byte[] {-128, 1, 25};
	};
	
	public byte[] getData2() {
		return new byte[] {-95, 6, -128, 1, 12, -127, 1, 24};
	};
	
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		//Option 1
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		TransferredVolumeRollOverImpl prim = new TransferredVolumeRollOverImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, TransferredVolumeImpl._ID_volumeIfNoTariffSwitch);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		assertTrue(prim.getIsPrimitive());
		assertNull(prim.getROVolumeIfTariffSwitch());
		assertEquals(prim.getROVolumeIfNoTariffSwitch().intValue(), 25);
		
		//Option 2
		data = this.getData2();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new TransferredVolumeRollOverImpl();
		prim.decodeAll(asn);
		assertEquals(tag, TransferredVolumeImpl._ID_volumeIfTariffSwitch);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
		assertFalse(prim.getIsPrimitive());
		assertNull(prim.getROVolumeIfNoTariffSwitch());
		assertEquals(prim.getROVolumeIfTariffSwitch().getROVolumeSinceLastTariffSwitch().intValue(), 12);
		assertEquals(prim.getROVolumeIfTariffSwitch().getROVolumeTariffSwitchInterval().intValue(), 24);
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		//Option 1
		TransferredVolumeRollOverImpl prim = new TransferredVolumeRollOverImpl(new Integer(25));
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
			
		//Option 2
		ROVolumeIfTariffSwitchImpl volumeIfTariffSwitch = new ROVolumeIfTariffSwitchImpl(new Integer(12), new Integer(24));	
		prim = new TransferredVolumeRollOverImpl(volumeIfTariffSwitch);
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
	}
	
}
