# bank-account-verification-acceptance-example

This is an example UI test suite for the Bank Account Verification Example Frontend using WebDriver and scalatest.

This test suite shows our recommended way of testing a service that integrates with the Bank Account Verification Frontend (BAVFE); it uses service manager and mocks to enable you
to bypass the BAVFE UI.

The benefits of this are as follows:
  
- Your tests don't break when we change stuff. 
- You have less test code because you don't need to create page objects and controls to drive our frontend.
- Your tests will run much faster since you are skipping out the part of your journey that uses our frontend.
- It's much easier to test different BAVFE response scenarios because you just set your expected response from BAVFE.  This is much easier than rather than creating lots of different scenarios that drive through BAVFE in different ways (which we will have covered in our tests anyway) to return different responses.

# Running the acceptance tests

Prior to executing the tests ensure you have:

- Appropriate webdriver binaries installed to run tests against your locally installed Browser(s) - If you don't have these you can
  use [docker containers instead](#running-specs-using-a-containerised-browser---on-a-developer-machine).
- Docker - If you want to run a browser (Chrome or Firefox) inside a container, or a ZAP container
- Installed/configured [service manager](https://github.com/hmrc/service-manager).

## Start the local services

To start services locally, run the following:

    sm --start BANK_ACCOUNT_VERIFICATION_FRONTEND_EXAMPLE AUTH_LOGIN_API AUTH_LOGIN_STUB AUTH USER_DETAILS IDENTITY_VERIFICATION -r --appendArgs '{                                                                                                              
      "BANK_ACCOUNT_VERIFICATION_FRONTEND_EXAMPLE": [
        "-J-Dauditing.enabled=true",
        "-J-Dmicroservice.services.bank-account-verification-api.port=6001",
        "-J-Dmicroservice.services.bank-account-verification-web.port=6001",
        "-J-Dauditing.consumer.baseUri.port=6001"
      ]
    }'

_**Important Note:** We are using port 6001 for the mock server. For the browser to be able to use the mock server for redirects on jenkins build jobs we need to use a port in the
range 6001 - 6010._

## Running specs

Execute the `run-specs.sh` script:

    ./run-specs.sh

The `run-specs.sh` script defaults to the locally installed `chrome` driver binary. For a complete list of supported param values, see:

- `src/test/resources/application.conf` for **environment**
- [webdriver-factory](https://github.com/hmrc/webdriver-factory#2-instantiating-a-browser-with-default-options) for **browser-driver**

## Running specs using a containerised browser - on a developer machine

The script `./run-local-browser-container.sh` can be used to start a Chrome or Firefox container on a developer machine.

Read more about the script's functionality [here](run-local-browser-container.sh), or invoke `./run-local-browser-container.sh -h`.

To run against a containerised Chrome browser:

```bash
./run-local-browser-container.sh --remote-chrome
./run-specs.sh remote-chrome
```

***Note:** `./run-local-browser-container.sh` should **NOT** be used when running in a CI environment!*

## Running ZAP specs - on a developer machine

You can use the `run-local-zap-container.sh` script to build a local ZAP container that will allow you to run ZAP tests locally.  
This will clone a copy of the dast-config-manager repository in this projects parent directory; it will require `make` to be available on your machine.  
https://github.com/hmrc/dast-config-manager/#running-zap-locally has more information about how the zap container is built.

```bash
./run-local-zap-container.sh --start
./run-local-browser-container.sh --remote-chrome
./run-local-zap-specs.sh
./run-local-zap-container.sh --stop
``` 

***Note:** Results of your ZAP run will not be placed in your target directory until you have run `./run-local-zap-container.sh --stop`*

***Note:** `./run-local-zap-container.sh` should **NOT** be used when running in a CI environment!*

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
