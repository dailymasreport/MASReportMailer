[#ftl]
<!DOCTYPE html>
<html>
<head>
  <style type="text/css">
		  	table, th, td {
		  	table-layout: fixed;
		    font-family: Arial,Helvetica,sans-serif;
		    border-collapse: collapse;
		    border: 1px solid white;
		    font-size:12px;
		    color: #FFFFFF;
		}

		td, th {
			
		    padding: 4px;
		    text-align: center;
		}

		th {
			background-color: #8064a2;
		}

		td.success {
			background-color: #009900;
		}

		td.error {
			background-color: #cc0000;
			font-weight: bolder;
		}

		td.warning {
			background-color: #f48c42;
		}

		td.benchmarkColumn {
			background-color: #e46d0a;	
		}

		td.mailerData {
			background-color: #538ed5;	
		}

		p {
			font-family: Arial,Helvetica,sans-serif;
			font-size:14px;
			color : #000000;
		}

		p.error {
			color: #FF0000;
		}

		.italic {
			font-style: italic;
		}

		.bold {
			font-weight: bold;	
		}
  </style>
</head>
<body>
<meta name="viewport" content="width=device-width, initial-scale=1">
<div>
	<p>Hi All,</p>
	<p>Please find below the MAS Report at ${time}.</p>
</div>

<div>
[#assign count = 0]
[#list mailData as data]
	
		[#if data.mailSentChange?contains('-')]
		<p class='error'>
			[#assign count = count+1]
			Observation ${count} - ${data.mailSentChange?replace("-", "")} dip has been observed in <span class='bold'>${data.mailerName}</span> till ${time} with a difference of ${data.mailSentBenchMark - data.mailsSent} lesser mails sent. Additionally, <span class='bold'>Open Rate</span> has
			[#if data.openRateBenchMark gt data.openRate]
				<span class='italic'> descreased </span>
			[#else]
				<span class='italic'> increased </span>
			[/#if]
			by ${data.openRateChange?replace("-", "")}.
		</p>
			[/#if]
			[#if data.mailSentBenchMark == 0 && data.mailsSent gt 0 && data.mailerName!="PRS"]
		<p class='error'>
				[#assign count = count+1]
				Observation ${count} - <span class='bold'>${data.mailerName}</span> mailer process seems to be running overnight.
		</p>
		[/#if]
	[/#list]
	[#if count < 1]
		<p class="italic">All mailers are working fine. Please find below the additional details.</p>
	[/#if]
</div>
<div align="left">
	<table>
		<tr>
			<th>Mailer Name</th>
			<th>Mails Sent</th>
			<th>Open Rate</th>
			<th>Mails Sent Benchmark</th>
			<th>Open Rate Benchmark</th>
			<th>Prev 2 week Avg Mails Sent</th>
			<th>Prev 2 week Avg Open Rate</th>
			<th>Mail Sent % Change (Based on Benchmark)</th>
			<th>Open Rate % Change (Based on Benchmark)</th>
		</tr>
		[#assign isNA = 0]
		[#list mailData as data]
		[#if data.mailSentBenchMark == 0 && data.mailsSent == 0]
		[#assign isNA = 1]
		<tr>			
		    <td class="mailerData">${data.mailerName}</td>
		    <td class="mailerData">NA**</td>
		    <td class="mailerData">NA**</td>
		    <td class="benchmarkColumn">NA**</td>
		    <td class="benchmarkColumn">NA**</td>
		    <td class="success">NA**</td>
		    <td	class="success">NA**</td>
		    <td	class="success">NA**</td>
		    <td	class="success">NA**</td>
	  	</tr>
	  	[#elseif data.mailSentBenchMark == 0 && data.mailsSent gt 0]
	  	[#assign isNA = 1]
		<tr>			
		    <td class="mailerData">${data.mailerName}</td>
		    <td class="mailerData">${data.mailsSent}</td>
		    <td class="mailerData">${data.openRate}</td>
		    <td class="benchmarkColumn">NA**</td>
		    <td class="benchmarkColumn">NA**</td>
		    <td	class="success">NA**</td>
		    <td	class="success">NA**</td>
		    <td	class="success">NA**</td>
		    <td	class="success">NA**</td>
		    <!--<td	class="warning">Process running overnight</td>-->
			<!--<td	class="warning">Process running overnight</td>-->	       
	  	</tr>
	  	[#else]
	  	<tr>
	    <td class="mailerData">${data.mailerName}</td>
	    <td class="mailerData">${data.mailsSent}</td>
		<td class="mailerData">${data.openRate}</td>
	    <td class="benchmarkColumn">${data.mailSentBenchMark}</td>
	    <td class="benchmarkColumn">${data.openRateBenchMark}</td>
	    <td 
	    [#if data.mailSentBenchMark gt data.mailsSentAvg]
	    	class="error"
	    [#else]
	    	class="success"
	    [/#if]
	    	>${data.mailsSentAvg}
    	</td>
	    <td 
	    [#if data.openRateBenchMark gt data.openRateAvg]
	    	class="error"
	    [#else]
	    	class="success"
	    [/#if]
	    	>${data.openRateAvg}
    	</td>
	    <td 
	    [#if data.mailSentChange?contains('-')]
	    	class="error"
	    [#else]
	    	class="success"
    	[/#if]
	    	>${data.mailSentChange}
    	</td>
	     <td 
	     [#if data.openRateChange?contains('-')]
	     	class="error"
	    [#else]
	    	class="success"
     	[/#if]
	    	>${data.openRateChange}
    	</td>
	  </tr>
	  [/#if]
	  [/#list]
	  </table>
	</div>
	<div>
		[#if isNA > 0]
	  		<p class="italic">NA** - Mail process for the respective mailer has not yet started till ${time}</p>
  		[/#if]
	</div>
	<div>
		<p>Best Regards,<br>MBQC Team</p>
	</div>
</body>
</html>	