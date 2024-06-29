package com.fmi.tournament.organizer.security.model;

public enum Permission {
  CREATE_TOURNAMENT,
  READ_ANY_TOURNAMENT,
  READ_OWNED_TOURNAMENT,
  MODIFY_ANY_TOURNAMENT,
  MODIFY_OWNED_TOURNAMENT,
  DELETE_ANY_TOURNAMENT,
  DELETE_OWNED_TOURNAMENT,
  REGISTER_FOR_TOURNAMENT,
  UNREGISTER_FROM_TOURNAMENT,
  CREATE_PARTICIPANT,
  READ_PARTICIPANT,
  UPDATE_ANY_PARTICIPANT,
  UPDATE_OWNED_PARTICIPANT,
  DELETE_ANY_PARTICIPANT,
  READ_ANY_AUTH_USER,
  DELETE_ANY_AUTH_USER
}