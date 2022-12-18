CREATE TABLE IF NOT EXISTS users (
	chat_id BIGINT PRIMARY KEY,
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	tg_username VARCHAR(255),
	registered_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
	category_id SERIAL PRIMARY KEY,
	title VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS expenses (
	expense_id BIGSERIAL PRIMARY KEY,
	user_id BIGINT NOT NULL,
	amount NUMERIC(11, 2) NOT NULL DEFAULT 0,
	category_id INT NOT NULL,
	added_on TIMESTAMP NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY(user_id) 
	REFERENCES users(chat_id) ON DELETE CASCADE,
	CONSTRAINT fk_category FOREIGN KEY(category_id)
	REFERENCES categories(category_id) ON DELETE CASCADE
);
