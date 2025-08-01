package net.xmx.vortex.physics.object.physicsobject.type.soft.pcmd;

import com.github.stephengold.joltjni.BodyInterface;
import com.github.stephengold.joltjni.Jolt;
import net.xmx.vortex.physics.object.physicsobject.manager.VxObjectManager;
import net.xmx.vortex.physics.world.VxPhysicsWorld;
import net.xmx.vortex.physics.world.pcmd.ICommand;

import java.util.UUID;

public record RemoveSoftBodyCommand(UUID objectId, int bodyId) implements ICommand {

    public static void queue(VxPhysicsWorld physicsWorld, UUID objectId, int bodyId) {
        physicsWorld.queueCommand(new RemoveSoftBodyCommand(objectId, bodyId));
    }

    @Override
    public void execute(VxPhysicsWorld world) {
        VxObjectManager objectManager = world.getObjectManager();
        if (world.getPhysicsSystem() == null || objectManager == null || bodyId == 0 || bodyId == Jolt.cInvalidBodyId) {
            return;
        }

        BodyInterface bodyInterface = world.getBodyInterface();
        if (bodyInterface != null && bodyInterface.isAdded(bodyId)) {
            bodyInterface.removeBody(bodyId);
            bodyInterface.destroyBody(bodyId);
        }

        objectManager.getObjectContainer().unlinkBodyId(bodyId);
    }
}