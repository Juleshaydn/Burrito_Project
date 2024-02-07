const express = require('express');
const stripe = require('stripe')('sk_test_51OfC7RKYr1gbOYbzF7eriNqEISiqqcEmt2eD3zGMv3Hiw4LbUDq07KPTWoLH2QbSSoJHZ9FmGlyXDDaLO1hLUgOK00scQm3MPe');
const bodyParser = require('body-parser');

const app = express();
const port = 3000;

// Middlewares
app.use(bodyParser.json());

// Route to create a PaymentIntent
app.post('/create-payment-intent', async (req, res) => {
    try {
        const { amount } = req.body;

        // Create a PaymentIntent with the order amount and currency
        const paymentIntent = await stripe.paymentIntents.create({
            amount,
            currency: 'usd',
        });

        res.json({
            clientSecret: paymentIntent.client_secret
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Start server
app.listen(port, () => {
    console.log(`Server listening at http://localhost:${port}`);
});
