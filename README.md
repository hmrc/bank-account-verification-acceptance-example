# bank-account-verification-acceptance-example

This is an example UI test suite for the Bank Account Verification Example Frontend using WebDriver and scalatest.

This test suite shows our recommended way of testing a service that integrates with the Bank Account Verification Frontend (BAVFE); it uses service manager and mocks to enable you to bypass the BAVFE UI.

The benefits of this are as follows:

 - Your tests will be faster since you don’t have to navigate through the BAVFE UI
 - Any changes we make to the BAVFE UI will not affect your acceptance tests
 - It’s much easier to tailor the the exact response you want to get from BAVFE to exercise various flows in your UI

## Running the tests

Prior to executing the tests ensure you have:
 - Docker - if you want to run a browser (Chrome or Firefox) inside a container 
 - Appropriate [drivers installed](#installing-local-driver-binaries) - to run tests against locally installed Browser
 - Installed/configured [service manager](https://github.com/hmrc/service-manager).  

Run the following command to start services locally:

    sm --start BANK_ACCOUNT_VERIFICATION_FRONTEND_EXAMPLE -r --appendArgs '{                                                                                                              
      "BANK_ACCOUNT_VERIFICATION_FRONTEND_EXAMPLE": [
        "-J-Dauditing.enabled=true",
        "-J-Dmicroservice.services.bank-account-verification-api.port=9000",
        "-J-Dmicroservice.services.bank-account-verification-web.port=9000"
      ]
    }
    '

Then execute the `run_tests.sh` script:
    
    ./run_tests.sh <browser-driver>

The `run_tests.sh` script defaults to the locally installed `chrome` driver binary.  For a complete list of supported param values, see:
 - `src/test/resources/application.conf` for **environment** 
 - [webdriver-factory](https://github.com/hmrc/webdriver-factory#2-instantiating-a-browser-with-default-options) for **browser-driver**

## Running tests against a containerised browser - on a developer machine

The script `./run-browser-with-docker.sh` can be used to start a Chrome or Firefox container on a developer machine. 
The script requires `remote-chrome` or `remote-firefox` as an argument.

Read more about the script's functionality [here](run-browser-with-docker.sh).

To run against a containerised Chrome browser:

```bash
./run-browser-with-docker.sh remote-chrome 
./run_tests.sh local remote-chrome
```

`./run-browser-with-docker.sh` is **NOT** required when running in a CI environment. 

## Installing local driver binaries

This project supports UI test execution using Firefox (Geckodriver) and Chrome (Chromedriver) browsers. 

See the `drivers/` directory for some helpful scripts to do the installation work for you.  They should work on both Mac and Linux by running the following command:

    ./installGeckodriver.sh <operating-system> <driver-version>
    or
    ./installChromedriver <operating-system> <driver-version>

- *<operating-system>* defaults to **linux64**, however it also supports **macos**
- *<driver-version>* defaults to **0.21.0** for Gecko/Firefox, and the latest release for Chrome.  You can, however, however pass any version available at the [Geckodriver](https://github.com/mozilla/geckodriver/tags) or [Chromedriver](http://chromedriver.storage.googleapis.com/) repositories.

**Note 1:** *You will need to ensure that you have a recent version of Chrome and/or Firefox installed for the later versions of the drivers to work reliably.*

**Note 2** *These scripts use sudo to set the right permissions on the drivers so you will likely be prompted to enter your password.*
