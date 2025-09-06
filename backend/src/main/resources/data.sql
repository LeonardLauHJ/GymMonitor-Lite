-- Initial data will be loaded from this file
-- For demo purposes, club with id 1 will be the most populated with data, 
-- and user with id 3 (ActiveGymGoer, active@dtf.com) will have the most detailed booking history.
-- all users will have the password: password
-- The gym class with id 14 has max capacity and can be used for testing the prevention of overbooking.

-- Clubs
INSERT INTO clubs (name, code, address) VALUES
('Downtown Fitness', 'DTF001', '123 Main St'),
('Uptown Gym', 'UTG002', '456 High St'),
('Suburb Gym', 'SBG003', '789 Side Rd');

-- Locations
INSERT INTO locations (club_id, name) VALUES
(1, 'Yoga Room'),
(1, 'Boxing Studio'),
(1, 'Main Hall'),
(2, 'Spin Zone'),
(2, 'Weight Room'),
(3, 'Cardio Room');

-- Membership Plans for Club 1
INSERT INTO membership_plans (club_id, name, billing_period_days, classes_per_week, price_cents, active) VALUES
(1, '3 Classes per Week (Paid Weekly)', 7, 3, 3000, true),
(1, '2 Classes per Week (Paid Fortnightly)', 14, 2, 6000, true),
(1, '4 Classes per Week (Paid Weekly)', 7, 4, 5000, true);

-- Membership Plans for Club 2
INSERT INTO membership_plans (club_id, name, billing_period_days, classes_per_week, price_cents, active) VALUES
(2, '2 Classes per Week (Paid Weekly)', 7, 2, 3200, true),
(2, '4 Classes per Week (Paid Weekly)', 7, 4, 5200, true);

-- All default users have the password: password

-- Users Club 1: Staff and Members
INSERT INTO users (club_id, membership_plan_id, name, role, email, password_hash, date_joined, cents_owed, next_billing_date) VALUES
(1, NULL, 'Alice Staff', 'STAFF', 'alice.staff@dtf.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '30 days', 0, NULL),
(1, NULL, 'Bob Staff', 'STAFF', 'bob.staff@dtf.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '25 days', 0, NULL),
(1, 1, 'ActiveGymGoer', 'MEMBER', 'active@dtf.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '60 days', 3000, NOW() + INTERVAL '1 days'),
(1, 2, 'CasualMember', 'MEMBER', 'casual@dtf.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '15 days', 1000, NOW() + INTERVAL '4 days'),
(1, 3, 'BusyBee', 'MEMBER', 'busybee@dtf.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '10 days', 0, NOW() + INTERVAL '6 days');

-- Users Club 2
INSERT INTO users (club_id, membership_plan_id, name, role, email, password_hash, date_joined, cents_owed, next_billing_date) VALUES
(2, NULL, 'Grace Staff', 'STAFF', 'grace.staff@utg.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '30 days', 0, NULL),
(2, NULL, 'OtherMember', 'MEMBER', 'other@utg.com', '$2a$10$.C/BaQSo7AgNENmMiDRSA.VmWM9q18e4GGyasdpFtHi0sfcXRcVyG', NOW() - INTERVAL '25 days', 0, NOW() + INTERVAL '3 days');

-- Gym Classes for ActiveGymGoer
INSERT INTO gym_classes (location_id, staff_id, name, description, start_time, end_time, max_capacity) VALUES
(1, 1, 'Morning Yoga', 'Gentle morning yoga session focusing on stretching, breathing, and relaxation to start your day refreshed.', NOW() - INTERVAL '30 days 07 hours', NOW() - INTERVAL '30 days 08 hours', 10),
(2, 2, 'Evening Boxing', 'Boxing basics for beginners with punching drills, footwork, and conditioning to improve strength and coordination.', NOW() - INTERVAL '29 days 18 hours', NOW() - INTERVAL '29 days 19 hours', 12),
(3, 1, 'Lunchtime HIIT', 'High intensity interval training to burn calories quickly, combining cardio bursts with strength exercises.', NOW() - INTERVAL '28 days 12 hours', NOW() - INTERVAL '28 days 12 hours' + INTERVAL '45 minutes', 15),
(1, 1, 'Afternoon Pilates', 'Pilates session targeting core strength, flexibility, and posture improvement in a calm environment.', NOW() - INTERVAL '27 days 15 hours', NOW() - INTERVAL '27 days 16 hours', 10),
(2, 2, 'Morning Spin', 'Cardio spin session to increase endurance, improve cardiovascular health, and burn calories effectively.', NOW() - INTERVAL '26 days 07 hours', NOW() - INTERVAL '26 days 08 hours', 12),
(3, 1, 'Evening Zumba', 'Dance your way to fitness with high-energy Zumba moves, fun choreography, and upbeat music.', NOW() - INTERVAL '25 days 18 hours', NOW() - INTERVAL '25 days 19 hours', 20),
(1, 1, 'Yoga Basics', 'Introductory yoga class for beginners, focusing on basic poses, breathing techniques, and relaxation.', NOW() - INTERVAL '24 days 08 hours', NOW() - INTERVAL '24 days 09 hours', 10),
(2, 2, 'Boxing Advanced', 'Advanced boxing techniques with sparring drills, combination punches, and footwork training.', NOW() - INTERVAL '23 days 18 hours', NOW() - INTERVAL '23 days 19 hours', 12),
(3, 1, 'HIIT Express', 'Quick high intensity workout for busy schedules, combining short bursts of cardio and strength moves.', NOW() - INTERVAL '22 days 12 hours', NOW() - INTERVAL '22 days 12 hours' + INTERVAL '30 minutes', 15),
(1, 1, 'Pilates Stretch', 'Full body stretch Pilates session to increase flexibility, mobility, and core stability.', NOW() - INTERVAL '21 days 15 hours', NOW() - INTERVAL '21 days 16 hours', 10),
(2, 2, 'Spin Sprint', 'Sprint intervals on the bike to build speed, endurance, and leg strength in a challenging session.', NOW() - INTERVAL '20 days 07 hours', NOW() - INTERVAL '20 days 08 hours', 12),
(2, 2, 'Strength Training', 'Full body strength training session with free weights and bodyweight exercises for overall fitness.', NOW() + INTERVAL '2 days 01 hours 15 minutes', NOW() + INTERVAL '2 days 02 hours', 5),
(3, 1, 'Pilates', 'Focused on improving flexibility, core strength, and balance through controlled Pilates exercises.', NOW() + INTERVAL '2 days 01 hours 15 minutes', NOW() + INTERVAL '2 days 03 hours', 10);

-- Bookings for ActiveGymGoer (user_id = 3)
INSERT INTO bookings (gym_class_id, member_id, booked_at, status) VALUES
(1, 3, NOW() - INTERVAL '31 days', 'COMPLETED'),
(2, 3, NOW() - INTERVAL '30 days', 'COMPLETED'),
(3, 3, NOW() - INTERVAL '29 days', 'COMPLETED'),
(4, 3, NOW() - INTERVAL '28 days', 'COMPLETED'),
(5, 3, NOW() - INTERVAL '27 days', 'COMPLETED'),
(6, 3, NOW() - INTERVAL '26 days', 'COMPLETED'),
(7, 3, NOW() - INTERVAL '25 days', 'COMPLETED'),
(8, 3, NOW() - INTERVAL '24 days', 'COMPLETED'),
(9, 3, NOW() - INTERVAL '23 days', 'CANCELLED'),
(10, 3, NOW() - INTERVAL '22 days', 'COMPLETED'),
(11, 3, NOW() - INTERVAL '21 days', 'COMPLETED'),
(12, 3, NOW() - INTERVAL '1 days', 'BOOKED'),
(13, 3, NOW() - INTERVAL '1 days', 'BOOKED');

-- Visits for ActiveGymGoer (user_id = 3)
INSERT INTO visits (member_id, club_id, scanned_at) VALUES
(3, 1, NOW() - INTERVAL '30 days 07 hours 05 minutes'),
(3, 1, NOW() - INTERVAL '29 days 18 hours 03 minutes'),
(3, 1, NOW() - INTERVAL '28 days 12 hours 02 minutes'),
(3, 1, NOW() - INTERVAL '27 days 15 hours 04 minutes'),
(3, 1, NOW() - INTERVAL '26 days 07 hours 01 minute'),
(3, 1, NOW() - INTERVAL '25 days 18 hours 03 minutes'),
(3, 1, NOW() - INTERVAL '24 days 08 hours 00 minutes'),
(3, 1, NOW() - INTERVAL '23 days 18 hours 02 minutes'),
(3, 1, NOW() - INTERVAL '22 days 12 hours 01 minute'),
(3, 1, NOW() - INTERVAL '21 days 15 hours 03 minutes');

-- Some additional bookings and visits for other Club 1 members

-- CasualMember (user_id = 4) booked and completed 2 classes
INSERT INTO bookings (gym_class_id, member_id, booked_at, status) VALUES
(1, 4, NOW() - INTERVAL '31 days', 'COMPLETED'),
(2, 4, NOW() - INTERVAL '30 days', 'COMPLETED');

INSERT INTO visits (member_id, club_id, scanned_at) VALUES
(4, 1, NOW() - INTERVAL '30 days 07 hours 05 minutes'),
(4, 1, NOW() - INTERVAL '29 days 18 hours 02 minutes');

-- BusyBee (user_id = 5) booked upcoming classes, filling out the MaxCapacityClass
INSERT INTO gym_classes (location_id, staff_id, name, description, start_time, end_time, max_capacity) VALUES
(1, 1, 'Bookable Yoga', 'Relaxing yoga session', NOW() + INTERVAL '15 days 07 hours', NOW() + INTERVAL '15 days 08 hours', 10),
(2, 2, 'MaxCapacityClass', 'This class does not have any more room to book', NOW() + INTERVAL '1 day 08 hours', NOW() + INTERVAL '1 day 09 hours', 1);

INSERT INTO bookings (gym_class_id, member_id, booked_at, status) VALUES
(14, 5, NOW() - INTERVAL '1 day', 'BOOKED'),
(15, 6, NOW() - INTERVAL '1 hour', 'BOOKED');

-- Visits for BusyBee for a past visit
INSERT INTO visits (member_id, club_id, scanned_at) VALUES
(5, 1, NOW() - INTERVAL '3 days 10 hours');

-- Classes for gym 1 to test timetable for specific day (all on 2050-01-01)
INSERT INTO gym_classes (location_id, staff_id, name, description, start_time, end_time, max_capacity) VALUES
(1, 1, 'New Year Yoga', 'Start 2050 with calm and focus in a gentle yoga session designed to relax the mind and stretch the body.', '2050-01-01 08:00:00', '2050-01-01 09:00:00', 15),
(2, 2, 'NYE HIIT Blast', 'High intensity training to kick off the year with fast-paced cardio and strength exercises to get your heart pumping.', '2050-01-01 10:00:00', '2050-01-01 11:00:00', 20),
(3, 1, 'New Year Zumba', 'Celebrate with fun dance cardio moves that improve fitness, coordination, and energy levels.', '2050-01-01 14:00:00', '2050-01-01 15:00:00', 25),
(1, 2, 'Midday Pilates', 'Core strengthening Pilates session focusing on posture, flexibility, and balance.', '2050-01-01 12:00:00', '2050-01-01 13:00:00', 20),
(2, 1, 'Evening Spin', 'Indoor cycling workout with music-driven intervals to build endurance and burn calories.', '2050-01-01 18:30:00', '2050-01-01 19:15:00', 18);


-- Class that occurs on the same day (and same time to ensure the test always works) as ActiveGymGoer's currently booked upcoming classes
-- (to test the maximum weekly bookings limit error)
-- (This class will have the id 21)
INSERT INTO gym_classes (location_id, staff_id, name, description, start_time, end_time, max_capacity) VALUES
(3, 1, 'Light Workout', 'A light workout to keep you active, suitable for all levels to maintain fitness without intense strain.', NOW() + INTERVAL '2 days 01 hours 15 minutes', NOW() + INTERVAL '2 days 02 hours', 10);

-- Another upcoming class booked by ActiveGymGoer to fill the dashboard
INSERT INTO gym_classes (location_id, staff_id, name, description, start_time, end_time, max_capacity) VALUES
(2, 2, 'Sunrise Bootcamp', 'High-energy bootcamp combining strength, cardio, and agility exercises to challenge every muscle group. Suitable for all levels.', NOW() + INTERVAL '7 days 06 hours', NOW() + INTERVAL '7 days 07 hours', 15);

INSERT INTO bookings (gym_class_id, member_id, booked_at, status) VALUES
(22, 3, NOW() - INTERVAL '7 days', 'BOOKED');