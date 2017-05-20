/*
 * Copyright (c) 2016. by Christian Felsing
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.felsing.client_cert.utilities;

import java.util.ArrayList;

public class SubjectAlternativeName {
    private final static ArrayList<String> san = new ArrayList<>();
    public final static String otherName = "otherName";
    public final static String rfc822Name = "rfc822Name";
    public final static String dNSName = "dNSName";
    public final static String x400Address = "x400Address";
    public final static String directoryName = "directoryName";
    public final static String ediPartyName = "ediPartyName";
    public final static String uniformResourceIdentifier = "uniformResourceIdentifier";
    public final static String iPAddress = "iPAddress";
    public final static String registeredID = "registeredID";

    static {
        san.add(otherName);
        san.add(rfc822Name);
        san.add(dNSName);
        san.add(x400Address);
        san.add(directoryName);
        san.add(ediPartyName);
        san.add(uniformResourceIdentifier);
        san.add(iPAddress);
        san.add(registeredID);
    }

    static String getSanOid(int oid) {
        try {
            return san.get(oid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} // class
