package com.fmi.tournament.organizer.security.model;

public enum Permission {
  CREATE_TOURNAMENT,
  READ_EVERY_TOURNAMENT,
  READ_OWNED_TOURNAMENT,
  MODIFY_EVERY_TOURNAMENT,
  MODIFY_OWNED_TOURNAMENT,
  DELETE_EVERY_TOURNAMENT,
  DELETE_OWNED_TOURNAMENT,
  REGISTER_FOR_TOURNAMENT,
  CREATE_PARTICIPANT,
  READ_PARTICIPANT,
  UPDATE_PARTICIPANT,
  DELETE_PARTICIPANT
}
