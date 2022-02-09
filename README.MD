<h1 align="center">
  <a href="https://github.com/orangebeard-io/soapui-listener">
    <img src="https://raw.githubusercontent.com/orangebeard-io/soapui-listener/main/.github/logo.svg" alt="Orangebeard.io SoapUI Listener" height="200">
  </a>
  <br>Orangebeard.io SoapUI Listener<br>
</h1>

<h4 align="center">Orangebeard listener for <a href="https://www.soapui.org/" target="_blank" rel="noopener">SoapUI</a> </h4>

<p align="center">
  <a href="https://repo.maven.apache.org/maven2/io/orangebeard/orangebeard-soapui-plugin/">
    <img src="https://img.shields.io/maven-central/v/io.orangebeard/orangebeard-soapui-plugin?style=flat-square"
      alt="MVN Version" />
  </a>
  <a href="https://github.com/orangebeard-io/soapui-listener/actions">
    <img src="https://img.shields.io/github/workflow/status/orangebeard-io/soapui-listener/release?style=flat-square"
      alt="Build Status" />
  </a>
  <a href="https://github.com/orangebeard-io/soapui-listener/blob/master/LICENSE.txt">
    <img src="https://img.shields.io/github/license/orangebeard-io/soapui-listener?style=flat-square"
      alt="License" />
  </a>
</p>

## Disclaimer
This listener is provided as-is as a proof-of-concept. Some features may be untested as long as the version number is below 1.x.x. 

## Usage

 - Obtain the jar from the releases page and put it in your `SOAPUI/bin/ext` directory
 - Put orangebeard-soapui-listeners.xml in your `SOAPUI/bin/listeners` directory
 - Add the following custom properties to the project you wish to report (save the properties to a .properties file and import them in soapUI, or set them manually):
```properties
orangebeard.endpoint=https://myhost.orangebeard.app
orangebeard.accessToken=my-access-token
orangebeard.project=my-orangebeard-project
orangebeard.testset=test-set-name
```


Now run your test and get the results in Orangebeard!
