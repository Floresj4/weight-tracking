# UI

Angular Single Page Application (SPA) interacting with weight tracking data.

### Local Development

This project use `npm-run-all` to launch multiple CLI tools (Angular CLI &amp; stubby4node) in parallel for local development.
- `ng serve` - to start the angular development server
- `stubby` - to start the mock service endpoint utility

The full list of arguments is added to `package.json`.

`npm-run-all -p start stubby` starts the angular development server and stubby in parallel.

#### Stubby cert

The certificate provided with stubby needs to be updated to key length of 2024.  The following command will generate a new certificate for the endpoint utility.

`openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout stubby.pem -out stubby.pem`

Add this cert to the `./mock` directory.  Configuration for this certificate is already added to the stubby script of `package.json`

- [Weight Tracking](../readme.md)
- [Batch](./batch) - The batch import application.
- [API](./api) - The request mapping interface for weight entries &ndash; management and presentation.