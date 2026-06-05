
INSERT INTO notes (id, title, content, created_at) VALUES
                                                       ('123e4567-e89b-12d3-a456-426614174000', 'Meeting Notes', 'Discuss project roadmap', '2024-05-20 10:00:00'),
                                                       ('123e4567-e89b-12d3-a456-426614174001', 'Shopping List', 'Buy milk, eggs, bread', '2024-05-20 11:00:00'),
                                                       ('123e4567-e89b-12d3-a456-426614174002', 'Work Tasks', 'Finish report, review PR', '2024-05-20 12:00:00');


INSERT INTO note_tags (note_id, tag) VALUES
                                         ('123e4567-e89b-12d3-a456-426614174000', 'work'),
                                         ('123e4567-e89b-12d3-a456-426614174000', 'meeting'),
                                         ('123e4567-e89b-12d3-a456-426614174001', 'personal'),
                                         ('123e4567-e89b-12d3-a456-426614174002', 'work'),
                                         ('123e4567-e89b-12d3-a456-426614174002', 'urgent');