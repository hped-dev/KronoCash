-- V2: Transacciones, metas de ahorro y hábitos

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME','EXPENSE')),
    category VARCHAR(100) NOT NULL,
    amount NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    description VARCHAR(500),
    transaction_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);

CREATE TABLE savings_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    target_amount NUMERIC(15,2) NOT NULL CHECK (target_amount > 0),
    current_amount NUMERIC(15,2) NOT NULL DEFAULT 0 CHECK (current_amount >= 0),
    deadline DATE NOT NULL
);

CREATE INDEX idx_savings_goals_user_id ON savings_goals(user_id);

CREATE TABLE habits (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    completed_today BOOLEAN NOT NULL DEFAULT FALSE,
    streak_count INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_habits_user_id ON habits(user_id);
