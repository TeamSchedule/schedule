create sequence task_sequence start 1 increment 1;
create sequence team_invite_sequence start 1 increment 1;
create sequence team_sequence start 1 increment 1;
create table app_user
(
  id int8 not null,
  primary key (id)
);
create table task
(
  id              int8    not null,
  closed          boolean not null,
  creation_time   timestamp,
  description     varchar(255),
  expiration_time timestamp,
  name            varchar(255),
  assignee_id     int8,
  author_id       int8,
  team_id         int8,
  primary key (id)
);
create table team
(
  id            int8 not null,
  creation_date date,
  name          varchar(255),
  admin_id      int8,
  primary key (id)
);
create table team_color
(
  team_id int8 not null,
  user_id int8 not null,
  color   varchar(255),
  primary key (team_id, user_id)
);
create table team_invite
(
  id            int8 not null,
  date          timestamp,
  invite_status varchar(255),
  invited_id    int8,
  inviting_id   int8,
  team_id       int8,
  primary key (id)
);
alter table task
  add constraint FKbmlrl2jnd3vcqmt3m8sh84ukm foreign key (assignee_id) references app_user;
alter table task
  add constraint FKhta5quwrp8540h5pokg3mxtgj foreign key (author_id) references app_user;
alter table task
  add constraint FK6r32b6vk1rpu7ww7gratmce1i foreign key (team_id) references team;
alter table team
  add constraint FKes0swtxvi8u7lon564b5l6oja foreign key (admin_id) references app_user;
alter table team_color
  add constraint FK4r8upj89w8lgk7t8fga37hw8f foreign key (team_id) references team;
alter table team_color
  add constraint FKg9nu8dr8b9yc7q5dr1onexlso foreign key (user_id) references app_user;
alter table team_invite
  add constraint FK4qf6il32lrel3nip7w74dastx foreign key (invited_id) references app_user;
alter table team_invite
  add constraint FK8uo93josawle96j6omyxfhmwd foreign key (inviting_id) references app_user;
alter table team_invite
  add constraint FKp9wtc66edyxm3i0mshq108h77 foreign key (team_id) references team;
