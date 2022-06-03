create sequence public.task_sequence start 1 increment 1;
create sequence public.team_invite_sequence start 1 increment 1;
create sequence public.team_sequence start 1 increment 1;
create table public.app_user
(
  id              int8 not null,
  default_team_id int8,
  primary key (id)
);
create table public.default_team
(
  color varchar(255),
  id    int8 not null,
  primary key (id)
);
create table public.public_team
(
  creation_date date,
  name          varchar(255),
  id            int8 not null,
  admin_id      int8,
  primary key (id)
);
create table public.task
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
create table public.team
(
  id int8 not null,
  primary key (id)
);
create table public.team_color
(
  team_id int8 not null,
  user_id int8 not null,
  color   varchar(255),
  primary key (team_id, user_id)
);
create table public.team_invite
(
  id            int8 not null,
  date          timestamp,
  invite_status varchar(255),
  invited_id    int8,
  inviting_id   int8,
  team_id       int8,
  primary key (id)
);
alter table public.app_user
  add constraint FKnkd2pog5iw9dphfd36b44pvld foreign key (default_team_id) references public.default_team;
alter table public.default_team
  add constraint FK44xgiihkdenkftsa9v7n31i6p foreign key (id) references public.team;
alter table public.public_team
  add constraint FK5i6n8a8rrdld3cs9gm4pv3hct foreign key (admin_id) references public.app_user;
alter table public.public_team
  add constraint FK5y3wir50w0lipkpmqteow9kw5 foreign key (id) references public.team;
alter table public.task
  add constraint FKbmlrl2jnd3vcqmt3m8sh84ukm foreign key (assignee_id) references public.app_user;
alter table public.task
  add constraint FKhta5quwrp8540h5pokg3mxtgj foreign key (author_id) references public.app_user;
alter table public.task
  add constraint FK6r32b6vk1rpu7ww7gratmce1i foreign key (team_id) references public.team;
alter table public.team_color
  add constraint FKlm9wpoufe4xx3wvx2unahl95d foreign key (team_id) references public.public_team;
alter table public.team_color
  add constraint FKg9nu8dr8b9yc7q5dr1onexlso foreign key (user_id) references public.app_user;
alter table public.team_invite
  add constraint FK4qf6il32lrel3nip7w74dastx foreign key (invited_id) references public.app_user;
alter table public.team_invite
  add constraint FK8uo93josawle96j6omyxfhmwd foreign key (inviting_id) references public.app_user;
alter table public.team_invite
  add constraint FKqy5rj0789bkct2f29x2133pl5 foreign key (team_id) references public.public_team;
