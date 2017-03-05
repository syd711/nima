package com.starsailor.actors;

import java.util.List;

/**
 * The core methods for providing formations
 */
public interface IFormationOwner<T extends IFormationMember> {

  List<T> getMembers();

  void addMember(T member);

  void removeMember(T member);
}
