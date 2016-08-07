package com.proj.iptools.port_scanner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Port_Data {
    private static HashMap<Integer, String> common_ports;
    static
    {
        common_ports = new HashMap<Integer, String>();
        common_ports.put(21, "File Transfer Protocol (FTP)");
        common_ports.put(22, "Secure Shell (SSH)");
        common_ports.put(23, "Telnet");
        common_ports.put(25, "Simple Mail Transfer Protocol (SMTP)");
        common_ports.put(53, "Domain Name System (DNS)");
        common_ports.put(80, "Hypertext Transfer Protocol (HTTP)");
        common_ports.put(81, "HOSTS2 Name Server (hosts2-ns)");
        common_ports.put(8080, "Hypertext Transfer Protocol (HTTP)");
        common_ports.put(110, "Post Office Protocol 3 (POP3)");
        common_ports.put(115, "Secure File Transfer Protocol SFTP");
        common_ports.put(123, "SFTP : Network Time Protocol (SFTP)");
        common_ports.put(135, "Remote Procedure Call (RPC)");
        common_ports.put(139, "Network Basic Input/Output System (NetBOIS) :");
        common_ports.put(143, "Internet Message Access Protocol (IMAP)");
        common_ports.put(161, "Simple Network Management Protocol (SNMP)");
        common_ports.put(194, "Internet Relay Chat IRC : ");
        common_ports.put(443, "Hypertext Transfer Protocol Secure (HTTPS)");
        common_ports.put(445, "Server Message Block (SMB)");
        common_ports.put(465, "Simple Mail Transfer Protocol over SSL (SMTPS)");
        common_ports.put(554, "Real Time Stream Control Protocol RTSP : ");
        common_ports.put(873, "RSYNC File Transfer Services RSYNC : ");
        common_ports.put(993, "Internet Message Access Protocol over SSL (IMAPS)");
        common_ports.put(995, "Post Office Protocol 3 over SSL (POP3)");
        common_ports.put(1433, "Microsoft SQL Server (MSSQL)");
        common_ports.put(3306, "MySQL");
        common_ports.put(3389, "Remote Desktop Protocol (RDP)");
        common_ports.put(5632, "PCAnywhere :");
        common_ports.put(5900, "Virtual Network Computing (VNC)");
        common_ports.put(6112, "Warcraft III");
    }

    /**
     * get channel from frequency
     * @param port
     * @return
     */
    public String getPortName(int port) {
        String port_name = "";
        Iterator it = common_ports.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if (entry.getKey().toString().equalsIgnoreCase(String.valueOf(port))) {
                port_name = entry.getValue().toString();
            }
        }
        return port_name;

    }
}
