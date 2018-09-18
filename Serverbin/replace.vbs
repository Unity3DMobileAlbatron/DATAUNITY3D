Const serverid = 61

Function GetIPAddress()
    Const strComputer = "."   ' Computer name. Dot means local computer
    Dim objWMIService, IPConfigSet, IPConfig, IPAddress, i
    Dim strIPAddress

    ' Connect to the WMI service
    Set objWMIService = GetObject("winmgmts:" _
        & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")

    ' Get all TCP/IP-enabled network adapters
    Set IPConfigSet = objWMIService.ExecQuery _
        ("Select * from Win32_NetworkAdapterConfiguration Where IPEnabled=TRUE")

    ' Get all IP addresses associated with these adapters
    For Each IPConfig In IPConfigSet
        IPAddress = IPConfig.IPAddress
        If Not IsNull(IPAddress) Then
			GetIPAddress = IPAddress(0)
			Exit Function
            'strIPAddress = strIPAddress & Join(IPAddress, ", ")
        End If
    Next
End Function


Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.OpenTextFile("gsd.xio.xml", 1)

strText = objFile.ReadAll
objFile.Close
strText = Replace(strText, "10010", (10100 + serverid) &  "")
strText = Replace(strText, "10161", (10100 + serverid) &  "")
strText = Replace(strText, "29000", (29000 + serverid) & "")
strText = Replace(strText, "29061", (29000 + serverid) & "")

Set objFile = objFSO.OpenTextFile("gsd.xio.xml", 2)
objFile.Write strText
objFile.Close

Set objFile = objFSO.OpenTextFile("gsd.config.xml", 1)

strText = objFile.ReadAll
objFile.Close

Set objFile = objFSO.OpenTextFile("gsd.config.xml", 2)
strText = Replace(strText, "serverid=""61""", "serverid=""" & serverid & """")
strText = Replace(strText, "SERVER_IP", GetIPAddress())
strText = Replace(strText, "127.0.0.1", GetIPAddress())
objFile.Write strText
objFile.Close