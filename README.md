# SMS AI Application

A Kotlin Spring Boot application that receives SMS messages via Twilio, processes them with Claude AI, and sends responses back via SMS.

## Features

- Receives SMS messages through Twilio webhooks
- Processes messages using Claude AI (Anthropic)
- Sends AI responses back via SMS
- Deployable on Google Cloud Platform
- Comprehensive error handling and logging

## Setup

### Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Twilio account with phone number
- Claude API key (Anthropic)
- Google Cloud Platform account (for deployment)

### Environment Variables

Copy `.env.example` to `.env` and fill in your credentials:

```bash
cp .env.example .env
```

Required environment variables:
- `TWILIO_ACCOUNT_SID`: Your Twilio Account SID
- `TWILIO_AUTH_TOKEN`: Your Twilio Auth Token  
- `TWILIO_PHONE_NUMBER`: Your Twilio phone number (e.g., +1234567890)
- `CLAUDE_API_KEY`: Your Claude API key

### Local Development

1. Clone the repository
2. Set up environment variables
3. Run the application:

```bash
./gradlew bootRun
```

The application will start on port 8080.

### Twilio Webhook Configuration

Configure your Twilio phone number webhook URL to point to:
```
https://your-domain.com/webhook/sms
```

## Deployment

### Google Cloud Run

1. Set up Google Cloud secrets:
```bash
gcloud secrets create twilio-account-sid --data-file=<(echo -n "your_account_sid")
gcloud secrets create twilio-auth-token --data-file=<(echo -n "your_auth_token")
gcloud secrets create twilio-phone-number --data-file=<(echo -n "your_phone_number")
gcloud secrets create claude-api-key --data-file=<(echo -n "your_claude_api_key")
```

2. Deploy using Cloud Build:
```bash
gcloud builds submit --config cloudbuild.yaml
```

### Docker

Build and run locally:
```bash
docker build -t smsai .
docker run -p 8080:8080 --env-file .env smsai
```

## API Endpoints

- `POST /webhook/sms` - Receives SMS messages from Twilio
- `GET /webhook/health` - Health check endpoint

## Architecture

- **WebhookController**: Handles incoming SMS webhooks from Twilio
- **ClaudeService**: Integrates with Claude AI API for message processing
- **SmsService**: Sends SMS responses via Twilio API
- **GlobalExceptionHandler**: Centralized error handling

## Security

- API keys are stored as environment variables
- Google Cloud deployment uses Secret Manager
- Comprehensive error handling prevents information leakage