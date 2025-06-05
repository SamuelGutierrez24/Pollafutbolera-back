/*
 Some of the tables are commented because they either don't exist yet
 in the model, or are inconsistent in the model to our current database schema.
 These tables are not currently being tested, these inconsistencies must be fixed before testing.
 */

-- Permissions
INSERT INTO permissions (id, name) VALUES
                                       (10, 'READ'),
                                       (11, 'WRITE'),
                                       (12, 'UPDATE'),
                                       (13, 'DELETE');

-- Roles
INSERT INTO roles (id, name) VALUES
                                 (10, 'ADMIN'),
                                 (11, 'USER');

-- Permission_Role (relación permisos y roles)
INSERT INTO permission_role (role_id, permission_id) VALUES
                                                         (10, 10), (10, 11), (10, 12), (10, 13), -- ADMIN: todos los permisos
                                                         (11, 10);                               -- USER: solo READ

-- Companies
INSERT INTO companies(id, name, nit, address, contact, deleted_at, logo) VALUES
    (10, 'ICESI University', '12345678-9' , 'address 1', 'contact 1', null, 'https://placehold.co/400'),
    (11, 'Acme Corporation', '98765432-1', 'address 2', 'contact 2', null, 'https://placehold.co/400'),
    (12, 'Tech Solutions Inc', '45678901-2', 'address 3', 'contact 3', null, 'https://placehold.co/400'),
    (13, 'Default Company', '0000000', null, null, null, 'https://placehold.co/400');


/*
-- Notification Channels
INSERT INTO notification_channel (id, name) VALUES
                                                (10, 'Email'),
                                                (11, 'SMS'),
                                                (12, 'WhatsApp');
*/

-- Teams
INSERT INTO teams (id, name, logo_url) VALUES
                                                        (10, 'Team A', 'https://placehold.co/400'),
                                                        (11, 'Team B', 'https://placehold.co/400'),
                                                        (12, 'Team C', 'https://placehold.co/400');

-- Tournaments
INSERT INTO tournaments (id, name, description) VALUES
                                                    (10, 'World Cup 2026', 'FIFA World Cup tournament'),
                                                    (11, 'Copa America 2024', 'South American football championship'),
                                                    (12, 'Champions', 'Champions League');

INSERT INTO tournaments (id, name, description,winner_team_id, top_scoring_team_id ) VALUES
                                                                                         (13, 'World Cup 2026', 'FIFA World Cup tournament',11,12);

INSERT INTO tournament_statistics (tournament_id, winner_team_id, fewest_goals_conceded_team_id, top_scoring_team_id) VALUES (13, 11, 12, 12);

-- Stages
INSERT INTO stages (id, stage_name,tournament_id) VALUES
                                        (10, 'Group Stage',13),
                                        (11, 'Quarter Finals',13),
                                        (12, 'Semi Finals',13),
                                        (13, 'Final',13);

-- Platform Configs
INSERT INTO platform_config (id, team_with_most_goals, exact_score, match_winner, tournament_champion)
VALUES
    (10, 5, 3, 1,10),
    (11, 4, 2, 1,10),
    (12, 7, 5, 2,10),
    (13, 7, 5, 2,10);

-- Pollas
INSERT INTO pollas (id, start_date, end_date, private, image_url, tournament_id, color, company_id)
VALUES
    (10, '2027-11-20 00:00:00', '2028-12-18 23:59:59', false, 'https://placehold.co/400', 10, 'blue', 10),
    (11, '2027-06-01 00:00:00', '2028-07-15 23:59:59', true, 'https://placehold.co/400', 11, 'green', 11),
    (12, '2027-01-01 00:00:00', '2027-12-31 23:59:59', false, 'https://placehold.co/400', 12, 'red', 12);


INSERT INTO pollas (id, start_date, end_date, private, image_url, tournament_id, color, company_id, platform_config_id)
VALUES
    (13, '2027-11-20 00:00:00', '2028-12-18 23:59:59', false, 'https://placehold.co/400', 13, 'blue', 10,13);



INSERT INTO rewards (id, name, description, image, position, polla_id)
VALUES
    (10, 'Gift Card', 'Amazon gift card', 'https://placehold.co/400', 1, 11),
    (11, 'Discount Coupon', '20% off coupon', 'https://placehold.co/400', 2, 11),
    (12, 'Free Subscription', '1 month free subscription', 'https://placehold.co/400', 1, 12);



-- Users
INSERT INTO users (id, cedula, company_id, name, last_name, password, mail, nickname, phone_number, role_id, notifications_email_enabled, notifications_sms_enabled, notifications_whatsapp_enabled)
VALUES
    (10, '1234567890', 10, 'Admin', 'User', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'admin@example.com', 'admin', '3001234567', 10, true,TRUE, TRUE ),
    (11, '0987654321', 11, 'Test', 'User', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'user@example.com', 'test', '3007654321', 11, true,TRUE, TRUE),
    (12, '5555555555', 12, 'Alice', 'Smith', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'alice@example.com', 'alice', '3001112222', 11, true, true, true),
    (13, '6666666666', 12, 'Ben', 'Smith', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'alice@example.com', 'ben', '3001112222', 11, true,TRUE, TRUE),
    (14, '7777777777', 13, 'Charlie', 'Brown', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'charlie@example.com', 'charlie', '3003334444', 11, true,TRUE, TRUE),
    (15, '8888888888', 11, 'Diana', 'Prince', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'diana@example.com', 'diana', '3005556666', 11, true,TRUE, TRUE),
    (16, '9999999999', 12, 'Eve', 'Adams', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'eve@example.com', 'eve', '3007778888', 11, true,TRUE, TRUE),
    (17, '1111111111', 10, 'Frank', 'Castle', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'frank@example.com', 'frank', '3009990000', 11, true,TRUE, TRUE),
    (18, '2222222222', 11, 'Grace', 'Hopper', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'grace@example.com', 'grace', '3001113333', 11, true,TRUE, TRUE),
    (19, '3333333333', 12, 'Hank', 'Pym', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'hank@example.com', 'hank', '3004445555', 11, true,TRUE, TRUE),
    (20, '4444444444', 10, 'Ivy', 'Green', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ivy@example.com', 'ivy', '3006667777', 11, true,TRUE, TRUE),
    (21, '5555555555', 11, 'Jack', 'Sparrow', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'jack@example.com', 'jack', '3008889999', 11, true,TRUE, TRUE),
    (22, '6666666666', 12, 'Karen', 'Page', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'karen@example.com', 'karen', '3001114444', 11, true,TRUE, TRUE),
    (23, '7777777777', 10, 'Luke', 'Cage', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'luke@example.com', 'luke', '3005557777', 11, true,TRUE, TRUE);



/*
-- User Notification Channels
INSERT INTO user_notification_channel (user_id, channel_id) VALUES
                                                                (10, 10), (10, 12),
                                                                (11, 11),
                                                                (12, 10), (12, 11);
*/
-- Subpollas
INSERT INTO subpollas (id, private, polla_id, creator_user_id) VALUES
                                                  (10, true, 11, 11),
                                                  (11, false, 11, 11),
                                                  (12, true, 12, 12);
-- User Scores Polla
INSERT INTO user_scores_polla (user_id, polla_id, scores, state)
VALUES
    (10, 10, 150, 'ACTIVO'),
    (11, 10, 120, 'ACTIVO'),
    (12, 12, 100, 'BLOQUEADO'),
    (13, 13, 0, 'ACTIVO'),
    (14, 11, 200, 'ACTIVO'),
    (15, 11, 155, 'ACTIVO'),
    (16, 11, 165, 'ACTIVO'),
    (17, 11, 160, 'ACTIVO'),
    (18, 11, 150, 'ACTIVO'),
    (19, 11, 100, 'ACTIVO'),
    (20, 11, 130, 'ACTIVO'),
    (21, 11, 120, 'ACTIVO'),
    (22, 11, 110, 'ACTIVO'),
    (23, 11, 190, 'ACTIVO');

INSERT INTO user_subpolla (user_id, subpolla_id)
VALUES
    (20, 11),
    (11, 11),
    (12, 11),
    (13, 11),
    (14, 11),
    (15, 11),
    (16, 11),
    (17, 11),
    (18, 11),
    (19, 11);



-- Tournament Bets
INSERT INTO tournament_bets (id, user_id, polla_id, earned_points, tournament_id, winner_team_id, top_scoring_team_id)
VALUES
    (10, 10, 10, 30, 10, 10, 12),
    (11, 11, 11, 20, 11, 11, 11),
    (12, 12, 12, 25, 12, 12, 10),
    (13, 13, 13, 15, 13, 12, 10);





-- Matches
INSERT INTO matches (id ,date, status, home_team_id, away_team_id, home_score, away_score, extra_time, penalty, winner_team_id, tournament_id, stage_id)
VALUES
    (10,'2024-06-20 16:00:00', 'FINISHED', 10, 11, 2, 1, false, false, 10, 11, 10), -- Team A vs Team B, Copa América, Grupo
    (11,'2024-06-24 18:00:00', 'FINISHED', 11, 12, 1, 3, false, false, 12, 11, 10), -- Team B vs Team C, Copa América, Grupo
    (12,'2024-07-01 20:00:00', 'FINISHED', 12, 10, 1, 1, true, true, 10, 11, 11),   -- Team C vs Team A, Cuartos, Team A gana por penales
    (13,'2026-12-18 10:00:00', 'SCHEDULED', 10, 12, NULL, NULL, false, false, NULL, 10, 13), -- World Cup Final, sin jugar
    (14,'2026-12-18 10:00:00', 'FINISHED', 11, 12, 1, 0, false, false, NULL, 13, 13),
    (15,'2026-12-18 10:00:00', 'FINISHED', 10, 11, 2, 1, false, false, NULL, 13, 13);


-- Match Bets
INSERT INTO match_bets (id, polla_id, home_score, away_score, earned_points, user_id, match_id)
VALUES
    (10, 10, 2, 1, 0, 10, 10),
    (11, 11, 1, 1, 0, 11, 11),
    (12, 12, 0, 3, 0, 12, 12),
    (13, 13, 1, 2, 0, 13, 14);

/*


-- Group Stage
INSERT INTO "group_stage" (id, group_name, tournament_id, first_winner_team_id, second_winner_team_id)
VALUES
    (10, 'Group A', 10, 10, 11),
    (11, 'Group B', 11, 11, 12);

-- Group Stage Match (relación entre grupo y partido)
INSERT INTO "group_stage_match" (group_id, match_id)
VALUES
    (10, 10),
    (11, 11);

-- Group Stage Team (equipos por grupo con puntos y ranking)
INSERT INTO "group_stage_team" (group_id, team_id, points, rank)
VALUES
    (10, 10, 9, 1),
    (10, 11, 6, 2),
    (11, 11, 7, 1),
    (11, 12, 4, 2);
*/
COMMIT;